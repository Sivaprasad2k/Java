package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.cache.Cache;
import com.sivaprasad.cacheforge.cache.InMemoryCache;

/**
 * CacheForge Phase 1 Demonstration & Verification Application.
 */
public class CacheForgeApplication {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CacheForge Engine - Phase 1 Verification");
        System.out.println("==========================================");

        Cache cache = new InMemoryCache();

        // 1. SET / PUT
        System.out.println("\n[1] Storing entries (SET)...");
        cache.put("user:101", "Siva");
        cache.put("user:102", "Prasad");
        cache.put("config:theme", "dark");
        System.out.println("Stored 3 key-value pairs.");

        // 2. SIZE
        System.out.println("\n[2] Checking size (SIZE)...");
        System.out.println("Current Cache Size: " + cache.size());

        // 3. GET
        System.out.println("\n[3] Retrieving entries (GET)...");
        System.out.println("GET user:101   -> " + cache.get("user:101"));
        System.out.println("GET user:102   -> " + cache.get("user:102"));
        System.out.println("GET user:999   -> " + cache.get("user:999") + " (Expected: null)");

        // 4. EXISTS
        System.out.println("\n[4] Checking key existence (EXISTS)...");
        System.out.println("EXISTS user:101 -> " + cache.containsKey("user:101"));
        System.out.println("EXISTS user:999 -> " + cache.containsKey("user:999"));

        // 5. DELETE
        System.out.println("\n[5] Removing entry (DELETE)...");
        boolean removed = cache.remove("user:102");
        System.out.println("DELETE user:102 -> Removed: " + removed);
        System.out.println("GET user:102    -> " + cache.get("user:102") + " (Expected: null)");
        System.out.println("Current Size    -> " + cache.size());

        // 6. CLEAR
        System.out.println("\n[6] Clearing cache (CLEAR)...");
        cache.clear();
        System.out.println("Cache Cleared. New Size: " + cache.size());
        System.out.println("EXISTS user:101 -> " + cache.containsKey("user:101"));

        System.out.println("\n==========================================");
        System.out.println(" Phase 1 Execution Successfully Completed!");
        System.out.println("==========================================");
    }
}
