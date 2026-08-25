package com.sivaprasad.cacheforge.eviction;

/**
 * Strategy interface defining eviction policy behavior.
 *
 * @param <K> Type of key managed by the eviction policy.
 */
public interface EvictionPolicy<K> {

    /**
     * Called whenever a key is accessed (GET).
     */
    void keyAccessed(K key);

    /**
     * Called whenever a key is inserted or updated (PUT).
     */
    void keyInserted(K key);

    /**
     * Called whenever a key is explicitly removed from the cache (REMOVE).
     */
    void keyRemoved(K key);

    /**
     * Identifies and removes the least recently used key according to the policy.
     *
     * @return The evicted key, or null if the policy tracking is empty.
     */
    K evictKey();

    /**
     * Clears all state maintained by the eviction policy.
     */
    void clear();
}
