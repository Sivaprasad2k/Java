package com.sivaprasad.cacheforge.cache;

/**
 * Domain entity wrapping a cached value with metadata such as timestamps and access metrics.
 *
 * @param <V> Type of value held in the entry.
 */
public class CacheEntry<V> {

    private V value;
    private final long createdAt;
    private long lastAccessedAt;
    private long accessCount;

    public CacheEntry(V value) {
        this.value = value;
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.lastAccessedAt = now;
        this.accessCount = 0;
    }

    public V getValue() {
        recordAccess();
        return value;
    }

    public void setValue(V value) {
        this.value = value;
        recordAccess();
    }

    public void recordAccess() {
        this.lastAccessedAt = System.currentTimeMillis();
        this.accessCount++;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getLastAccessedAt() {
        return lastAccessedAt;
    }

    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
                "value=" + value +
                ", createdAt=" + createdAt +
                ", lastAccessedAt=" + lastAccessedAt +
                ", accessCount=" + accessCount +
                '}';
    }
}
