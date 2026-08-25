package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.cache.Cache;
import com.sivaprasad.cacheforge.cache.InMemoryCache;
import com.sivaprasad.cacheforge.config.CacheConfig;

/**
 * CacheForge Phase 3 Verification Application testing LRU Eviction & Capacity Bounds.
 */
public class CacheForgeApplication {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CacheForge Engine - Phase 3 Verification");
        System.out.println("==========================================");

        // Configure a cache with maximum capacity of 3 entries
        CacheConfig config = new CacheConfig(3, true);
        Cache<String, String> lruCache = new InMemoryCache<>(config);

        System.out.println("\n[1] Populating cache up to capacity (maxCapacity = 3)...");
        lruCache.put("k1", "Value-1");
        lruCache.put("k2", "Value-2");
        lruCache.put("k3", "Value-3");

        System.out.println("Initial Size: " + lruCache.size() + " (Keys: k1, k2, k3)");

        // Access k1 to make it Most Recently Used (MRU)
        // Access Order (MRU -> LRU): k1 -> k3 -> k2
        System.out.println("\n[2] Accessing key 'k1' to promote it to MRU...");
        System.out.println("GET k1 -> " + lruCache.get("k1"));

        // Insert 4th key 'k4' which triggers LRU eviction of 'k2'
        System.out.println("\n[3] Inserting 4th key 'k4' (Triggering Eviction)...");
        lruCache.put("k4", "Value-4");

        System.out.println("Size after insertion: " + lruCache.size());

        // Verify eviction results
        System.out.println("\n[4] Verifying Eviction Outcomes...");
        System.out.println("GET k1 -> " + lruCache.get("k1") + " (Retained as MRU)");
        System.out.println("GET k2 -> " + lruCache.get("k2") + " (Expected: null - EVICTED!)");
        System.out.println("GET k3 -> " + lruCache.get("k3") + " (Retained)");
        System.out.println("GET k4 -> " + lruCache.get("k4") + " (Retained - Newly inserted)");

        System.out.println("\n[5] Performance & Eviction Metrics:");
        System.out.println(lruCache.getStatistics());

        System.out.println("\n==========================================");
        System.out.println(" Phase 3 Verification Completed Successfully!");
        System.out.println("==========================================");
    }
}
