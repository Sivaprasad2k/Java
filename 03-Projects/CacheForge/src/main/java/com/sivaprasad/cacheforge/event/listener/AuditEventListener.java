package com.sivaprasad.cacheforge.event.listener;

import com.sivaprasad.cacheforge.event.CacheEvent;
import com.sivaprasad.cacheforge.event.CacheEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Event listener that records published events into an audit log trail.
 */
public class AuditEventListener<K, V> implements CacheEventListener<K, V> {

    private final List<CacheEvent<K, V>> auditLog;

    public AuditEventListener() {
        this.auditLog = new CopyOnWriteArrayList<>();
    }

    @Override
    public void onEvent(CacheEvent<K, V> event) {
        if (event != null) {
            auditLog.add(event);
        }
    }

    public List<CacheEvent<K, V>> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }

    public int getEventCount() {
        return auditLog.size();
    }

    public void clear() {
        auditLog.clear();
    }
}
