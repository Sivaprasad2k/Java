package com.sivaprasad.cacheforge.analytics;

import com.sivaprasad.cacheforge.cache.CacheEntry;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Executes analytical queries over cache storage data using Java Streams API.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class CacheAnalytics<K, V> {

    private final Map<K, CacheEntry<V>> storage;

    public CacheAnalytics(Map<K, CacheEntry<V>> storage) {
        this.storage = storage;
    }

    /**
     * Returns top N most frequently accessed keys sorted in descending order of access count.
     */
    public List<K> getTopAccessedKeys(int topN) {
        if (topN <= 0 || storage.isEmpty()) return List.of();
        return storage.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue().getAccessCount(), e1.getValue().getAccessCount()))
                .map(Map.Entry::getKey)
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the average remaining TTL in seconds for all active entries with a TTL configured.
     */
    public double getAverageTtlSeconds() {
        if (storage.isEmpty()) return 0.0;
        return storage.values().stream()
                .mapToLong(CacheEntry::getTtlSeconds)
                .filter(ttl -> ttl > 0)
                .average()
                .orElse(0.0);
    }

    /**
     * Groups keys by namespace prefix (e.g. "user:101" -> namespace "user").
     */
    public Map<String, List<K>> getKeysGroupedByNamespace() {
        if (storage.isEmpty()) return Map.of();
        return storage.keySet().stream()
                .collect(Collectors.groupingBy(key -> {
                    String strKey = String.valueOf(key);
                    int colonIndex = strKey.indexOf(':');
                    return colonIndex != -1 ? strKey.substring(0, colonIndex) : "default";
                }));
    }

    /**
     * Filters cache keys matching a user-provided Predicates condition.
     */
    public List<K> filterKeys(Predicate<K> predicate) {
        if (predicate == null || storage.isEmpty()) return List.of();
        return storage.keySet().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Computes the cumulative total number of reads across all current entries.
     */
    public long getTotalAccesses() {
        return storage.values().stream()
                .mapToLong(CacheEntry::getAccessCount)
                .sum();
    }
}
