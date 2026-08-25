package com.sivaprasad.cacheforge.cache;

import com.sivaprasad.cacheforge.metrics.CacheStatistics;

/**
 * Generic Cache interface defining type-safe storage operations and metric reporting.
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
     * Retrieves the value associated with the given key (GET).
     * @return Value associated with key, or null if key does not exist.
     */
    V get(K key);

    /**
     * Removes the key-value pair associated with the given key (DELETE).
     * @return true if an entry was removed, false otherwise.
     */
    boolean remove(K key);

    /**
     * Checks whether a key exists in the cache (EXISTS).
     */
    boolean containsKey(K key);

    /**
     * Clears all entries from the cache (CLEAR).
     */
    void clear();

    /**
     * Returns the current number of entries stored in the cache (SIZE).
     */
    int size();

    /**
     * Returns operational statistics for this cache instance.
     */
    CacheStatistics getStatistics();
}
