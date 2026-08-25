package com.sivaprasad.cacheforge.event.listener;

import com.sivaprasad.cacheforge.event.CacheEvent;
import com.sivaprasad.cacheforge.event.CacheEventListener;

/**
 * Event listener that logs cache mutation events to stdout.
 */
public class LoggingEventListener<K, V> implements CacheEventListener<K, V> {

    @Override
    public void onEvent(CacheEvent<K, V> event) {
        System.out.printf("[EVENT LOG] Type: %-15s | Key: %-10s | Value: %s\n",
                event.getType(), event.getKey(), event.getValue());
    }
}
