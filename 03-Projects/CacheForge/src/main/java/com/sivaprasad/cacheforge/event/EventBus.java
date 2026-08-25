package com.sivaprasad.cacheforge.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-process event dispatcher managing listener subscriptions and broadcasting cache events.
 * Uses a CopyOnWriteArrayList for thread-safe concurrent iteration without blocking registers.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class EventBus<K, V> {

    private final List<CacheEventListener<K, V>> listeners;

    public EventBus() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(CacheEventListener<K, V> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(CacheEventListener<K, V> listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void publishEvent(CacheEvent<K, V> event) {
        if (event == null || listeners.isEmpty()) return;
        for (CacheEventListener<K, V> listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                System.err.println("[EventBus] Error in listener execution: " + e.getMessage());
            }
        }
    }

    public int getListenerCount() {
        return listeners.size();
    }
}
