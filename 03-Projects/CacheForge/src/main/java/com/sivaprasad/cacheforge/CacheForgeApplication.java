package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.analytics.CacheAnalytics;
import com.sivaprasad.cacheforge.cache.InMemoryCache;
import com.sivaprasad.cacheforge.config.CacheConfig;

import java.util.List;
import java.util.Map;

/**
 * CacheForge Phase 7 Functional Analytics & Streams API Verification Application.
 */
public class CacheForgeApplication {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CacheForge Engine - Phase 7 Verification");
        System.out.println("==========================================");

        CacheConfig config = new CacheConfig(100, true);
        InMemoryCache<String, String> cache = new InMemoryCache<>(config);

        System.out.println("\n[1] Populating Cache entries across namespaces...");
        cache.put("user:101", "Siva");
        cache.put("user:102", "Prasad");
        cache.put("user:103", "Avis");
        cache.put("config:theme", "dark", 60);
        cache.put("config:font", "Inter", 120);
        cache.put("session:99", "AUTH_TOKEN_ABC", 30);

        // Simulate varied access frequencies
        System.out.println("\n[2] Simulating access frequencies...");
        for (int i = 0; i < 5; i++) cache.get("user:101");
        for (int i = 0; i < 3; i++) cache.get("user:102");
        for (int i = 0; i < 1; i++) cache.get("config:theme");

        CacheAnalytics<String, String> analytics = cache.getAnalytics();

        // 1. Top N Most Frequently Accessed Keys
        System.out.println("\n[3] Streams Query: Top 3 Most Frequently Accessed Keys:");
        List<String> topKeys = analytics.getTopAccessedKeys(3);
        topKeys.forEach(key ->
            System.out.println("  -> Key: " + key + " | Access Count: " + cache.getAnalytics().getTotalAccesses())
        );

        // 2. Average TTL Calculation
        System.out.println("\n[4] Streams Query: Average Active TTL (Seconds):");
        System.out.printf("  -> Average TTL: %.2f seconds\n", analytics.getAverageTtlSeconds());

        // 3. Namespace Grouping (groupingBy)
        System.out.println("\n[5] Streams Query: Keys Grouped by Namespace Prefix:");
        Map<String, List<String>> grouped = analytics.getKeysGroupedByNamespace();
        grouped.forEach((namespace, keys) ->
            System.out.println("  -> Namespace [" + namespace + "]: " + keys)
        );

        // 4. Custom Predicate Filter
        System.out.println("\n[6] Streams Query: Filter Keys starting with 'user':");
        List<String> userKeys = analytics.filterKeys(k -> k.startsWith("user"));
        System.out.println("  -> Matching User Keys: " + userKeys);

        // 5. Total Read Accesses (reduce / mapToLong)
        System.out.println("\n[7] Streams Query: Cumulative Total Read Accesses:");
        System.out.println("  -> Total Accesses: " + analytics.getTotalAccesses());

        cache.shutdown();

        System.out.println("\n==========================================");
        System.out.println(" Phase 7 Verification Completed Successfully!");
        System.out.println("==========================================");
    }
}
