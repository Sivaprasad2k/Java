package com.sivaprasad.cacheforge.cache;

/**
 * Basic Cache interface defining core key-value storage operations.
 */
public interface Cache {

    /**
     * Stores a key-value pair in the cache (SET).
     */
    void put(String key, String value);

    /**
     * Retrieves the value associated with the given key (GET).
     * @return Value associated with key, or null if key does not exist.
     */
    String get(String key);

    /**
     * Removes the key-value pair associated with the given key (DELETE).
     * @return true if an entry was removed, false otherwise.
     */
    boolean remove(String key);

    /**
     * Checks whether a key exists in the cache (EXISTS).
     */
    boolean containsKey(String key);

    /**
     * Clears all entries from the cache (CLEAR).
     */
    void clear();

    /**
     * Returns the current number of entries stored in the cache (SIZE).
     */
    int size();
}
