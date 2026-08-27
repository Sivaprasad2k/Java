package com.sivaprasad.cacheforge.cache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Domain entity wrapping a cached value with metadata such as timestamps, access metrics, and TTL expiration.
 * Thread-safe implementation utilizing volatile visibility and AtomicLong access counters.
 *
 * @param <V> Type of value held in the entry.
 */
public class CacheEntry<V> {

    private volatile V value;
    private final long createdAt;
    private volatile long lastAccessedAt;
    private final AtomicLong accessCount;
    private volatile long expireAtTimestamp; // -1 indicates no expiration

    public CacheEntry(V value) {
        this.value = value;
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.lastAccessedAt = now;
        this.accessCount = new AtomicLong(0);
        this.expireAtTimestamp = -1;
    }

    public V getValue() {
        recordAccess();
        return value;
    }

    public V peekValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
        recordAccess();
    }

    public void recordAccess() {
        this.lastAccessedAt = System.currentTimeMillis();
        this.accessCount.incrementAndGet();
    }

    public void setTtlSeconds(long seconds) {
        if (seconds > 0) {
            this.expireAtTimestamp = System.currentTimeMillis() + (seconds * 1000);
        } else {
            this.expireAtTimestamp = -1;
        }
    }

    public boolean isExpired() {
        long expireAt = this.expireAtTimestamp;
        return expireAt != -1 && System.currentTimeMillis() > expireAt;
    }

    public long getTtlSeconds() {
        long expireAt = this.expireAtTimestamp;
        if (expireAt == -1) {
            return -1; // No expiration configured
        }
        long remainingMs = expireAt - System.currentTimeMillis();
        if (remainingMs <= 0) {
            return -2; // Expired
        }
        return (remainingMs + 999) / 1000; // Ceil remaining seconds
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getLastAccessedAt() {
        return lastAccessedAt;
    }

    public long getAccessCount() {
        return accessCount.get();
    }

    public long getExpireAtTimestamp() {
        return expireAtTimestamp;
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
                "value=" + value +
                ", createdAt=" + createdAt +
                ", lastAccessedAt=" + lastAccessedAt +
                ", accessCount=" + accessCount.get() +
                ", expireAtTimestamp=" + expireAtTimestamp +
                '}';
    }
}
