package com.sivaprasad.cacheforge.event;

/**
 * Event payload object representing a cache mutation event.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class CacheEvent<K, V> {

    private final EventType type;
    private final K key;
    private final V value;
    private final long timestamp;

    public CacheEvent(EventType type, K key, V value) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public EventType getType() {
        return type;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "CacheEvent{" +
                "type=" + type +
                ", key=" + key +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}
