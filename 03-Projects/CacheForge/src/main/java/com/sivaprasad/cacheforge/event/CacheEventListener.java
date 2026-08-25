package com.sivaprasad.cacheforge.event;

/**
 * Functional interface implemented by subscribers listening for cache events.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
@FunctionalInterface
public interface CacheEventListener<K, V> {

    /**
     * Callback invoked when a CacheEvent is published by the EventBus.
     */
    void onEvent(CacheEvent<K, V> event);
}
