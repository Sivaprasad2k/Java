package com.sivaprasad.cacheforge.cache;

import com.sivaprasad.cacheforge.config.CacheConfig;
import com.sivaprasad.cacheforge.eviction.EvictionPolicy;
import com.sivaprasad.cacheforge.eviction.LruEvictionPolicy;
import com.sivaprasad.cacheforge.metrics.CacheStatistics;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic in-memory cache implementation supporting capacity bounds, LRU eviction, and statistics collection.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class InMemoryCache<K, V> implements Cache<K, V> {

    private final Map<K, CacheEntry<V>> storage;
    private final CacheConfig config;
    private final CacheStatistics statistics;
    private final EvictionPolicy<K> evictionPolicy;

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
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Cache key cannot be null");
        }

        CacheEntry<V> existing = storage.get(key);
        if (existing != null) {
            existing.setValue(value);
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
            storage.put(key, new CacheEntry<>(value));
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
        return storage.containsKey(key);
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

    public CacheConfig getConfig() {
        return config;
    }
}
