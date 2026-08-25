package com.sivaprasad.cacheforge.event;

/**
 * Event payload object representing a cache mutation event.
 * Java 17 Generic Record implementation.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public record CacheEvent<K, V>(EventType type, K key, V value, long timestamp) {

    public CacheEvent(EventType type, K key, V value) {
        this(type, key, value, System.currentTimeMillis());
    }

    public EventType getType() {
        return type();
    }

    public K getKey() {
        return key();
    }

    public V getValue() {
        return value();
    }

    public long getTimestamp() {
        return timestamp();
    }
}
