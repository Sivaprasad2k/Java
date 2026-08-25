package com.sivaprasad.cacheforge.cache;

import com.sivaprasad.cacheforge.metrics.CacheStatistics;

/**
 * Generic Cache interface defining type-safe storage operations, TTL expiration, and metric reporting.
 *
 * @param <K> Type of key.
 * @param <V> Type of value.
 */
public interface Cache<K, V> {

    /**
     * Stores a key-value pair in the cache (SET).
     */
    void put(K key, V value);

    /**
     * Stores a key-value pair in the cache with a specified TTL in seconds (SET with EXPIRE).
     */
    void put(K key, V value, long ttlSeconds);

    /**
     * Retrieves the value associated with the given key (GET).
     * @return Value associated with key, or null if key does not exist or has expired.
     */
    V get(K key);

    /**
     * Removes the key-value pair associated with the given key (DELETE).
     * @return true if an entry was removed, false otherwise.
     */
    boolean remove(K key);

    /**
     * Checks whether a key exists and has not expired (EXISTS).
     */
    boolean containsKey(K key);

    /**
     * Sets a time-to-live expiration on an existing key (EXPIRE key seconds).
     * @return true if TTL was successfully configured, false if key does not exist or has expired.
     */
    boolean expire(K key, long seconds);

    /**
     * Retrieves the remaining time-to-live (TTL) for a key in seconds (TTL key).
     * @return Remaining TTL in seconds, -1 if key has no expiration, or -2 if key does not exist/expired.
     */
    long getTtl(K key);

    /**
     * Clears all entries from the cache (CLEAR).
     */
    void clear();

    /**
     * Returns the current number of valid non-expired entries stored in the cache (SIZE).
     */
    int size();

    /**
     * Returns operational statistics for this cache instance.
     */
    CacheStatistics getStatistics();

    /**
     * Shuts down background tasks (e.g. active expiration workers).
     */
    void shutdown();
}
