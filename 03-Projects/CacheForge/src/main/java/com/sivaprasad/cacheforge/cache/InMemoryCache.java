package com.sivaprasad.cacheforge.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic in-memory cache implementation backed by a Java HashMap.
 * Initial implementation for Phase 1 (Single-threaded baseline correctness).
 */
public class InMemoryCache implements Cache {

    private final Map<String, String> storage;

    public InMemoryCache() {
        this.storage = new HashMap<>();
    }

    @Override
    public void put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("Cache key cannot be null");
        }
        storage.put(key, value);
    }

    @Override
    public String get(String key) {
        if (key == null) {
            return null;
        }
        return storage.get(key);
    }

    @Override
    public boolean remove(String key) {
        if (key == null) {
            return false;
        }
        return storage.remove(key) != null;
    }

    @Override
    public boolean containsKey(String key) {
        if (key == null) {
            return false;
        }
        return storage.containsKey(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }
}
