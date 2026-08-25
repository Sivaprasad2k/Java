package com.sivaprasad.cacheforge.cache;

import com.sivaprasad.cacheforge.config.CacheConfig;
import com.sivaprasad.cacheforge.eviction.EvictionPolicy;
import com.sivaprasad.cacheforge.eviction.LruEvictionPolicy;
import com.sivaprasad.cacheforge.expiration.ExpirationManager;
import com.sivaprasad.cacheforge.metrics.CacheStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic in-memory cache implementation supporting LRU eviction, passive/active TTL expiration, and metrics.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class InMemoryCache<K, V> implements Cache<K, V> {

    private final Map<K, CacheEntry<V>> storage;
    private final CacheConfig config;
    private final CacheStatistics statistics;
    private final EvictionPolicy<K> evictionPolicy;
    private final ExpirationManager expirationManager;

    public InMemoryCache() {
        this(new CacheConfig(), new LruEvictionPolicy<>());
    }

    public InMemoryCache(CacheConfig config) {
        this(config, new LruEvictionPolicy<>());
    }

    public InMemoryCache(CacheConfig config, EvictionPolicy<K> evictionPolicy) {
        if (config == null) {
            throw new IllegalArgumentException("CacheConfig cannot be null");
        }
        if (evictionPolicy == null) {
            throw new IllegalArgumentException("EvictionPolicy cannot be null");
        }
        this.config = config;
        this.evictionPolicy = evictionPolicy;
        this.storage = new HashMap<>();
        this.statistics = new CacheStatistics();
        this.expirationManager = new ExpirationManager();

        // Start active background expiration scanner running every 500 ms
        this.expirationManager.startActiveCleanup(this::performActiveExpirationScan, 500);
    }

    @Override
    public void put(K key, V value) {
        put(key, value, -1);
    }

    @Override
    public void put(K key, V value, long ttlSeconds) {
        if (key == null) {
            throw new IllegalArgumentException("Cache key cannot be null");
        }

        CacheEntry<V> existing = storage.get(key);
        if (existing != null) {
            existing.setValue(value);
            existing.setTtlSeconds(ttlSeconds);
            evictionPolicy.keyInserted(key);
        } else {
            // Check capacity limit and evict LRU entry if full
            if (storage.size() >= config.getMaxCapacity()) {
                K evictedKey = evictionPolicy.evictKey();
                if (evictedKey != null) {
                    storage.remove(evictedKey);
                    statistics.recordEviction();
                }
            }
            CacheEntry<V> newEntry = new CacheEntry<>(value);
            newEntry.setTtlSeconds(ttlSeconds);
            storage.put(key, newEntry);
            evictionPolicy.keyInserted(key);
        }
        statistics.recordPut();
    }

    @Override
    public V get(K key) {
        if (key == null) {
            statistics.recordMiss();
            return null;
        }
        CacheEntry<V> entry = storage.get(key);
        if (entry != null) {
            if (checkAndEvictExpired(key, entry)) {
                statistics.recordMiss();
                return null;
            }
            evictionPolicy.keyAccessed(key);
            statistics.recordHit();
            return entry.getValue();
        } else {
            statistics.recordMiss();
            return null;
        }
    }

    @Override
    public boolean remove(K key) {
        if (key == null) {
            return false;
        }
        CacheEntry<V> removed = storage.remove(key);
        if (removed != null) {
            evictionPolicy.keyRemoved(key);
            statistics.recordRemoval();
            return true;
        }
        return false;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        CacheEntry<V> entry = storage.get(key);
        if (entry != null) {
            if (checkAndEvictExpired(key, entry)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean expire(K key, long seconds) {
        if (key == null) return false;
        CacheEntry<V> entry = storage.get(key);
        if (entry == null || checkAndEvictExpired(key, entry)) {
            return false;
        }
        entry.setTtlSeconds(seconds);
        return true;
    }

    @Override
    public long getTtl(K key) {
        if (key == null) return -2;
        CacheEntry<V> entry = storage.get(key);
        if (entry == null || checkAndEvictExpired(key, entry)) {
            return -2; // Key does not exist or expired
        }
        return entry.getTtlSeconds();
    }

    @Override
    public void clear() {
        storage.clear();
        evictionPolicy.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public CacheStatistics getStatistics() {
        return statistics;
    }

    @Override
    public void shutdown() {
        expirationManager.shutdown();
    }

    public CacheConfig getConfig() {
        return config;
    }

    // Lazy Expiration Helper - checks if entry is expired and removes it immediately
    private boolean checkAndEvictExpired(K key, CacheEntry<V> entry) {
        if (entry.isExpired()) {
            storage.remove(key);
            evictionPolicy.keyRemoved(key);
            statistics.recordExpiration();
            return true;
        }
        return false;
    }

    // Active Background Expiration Scanner Task
    private void performActiveExpirationScan() {
        if (storage.isEmpty()) return;
        List<K> expiredKeys = new ArrayList<>();
        for (Map.Entry<K, CacheEntry<V>> mapEntry : storage.entrySet()) {
            if (mapEntry.getValue().isExpired()) {
                expiredKeys.add(mapEntry.getKey());
            }
        }
        for (K expiredKey : expiredKeys) {
            if (storage.remove(expiredKey) != null) {
                evictionPolicy.keyRemoved(expiredKey);
                statistics.recordExpiration();
            }
        }
    }
}
