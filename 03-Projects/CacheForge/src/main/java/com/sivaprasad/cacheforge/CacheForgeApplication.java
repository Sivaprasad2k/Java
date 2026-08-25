package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.cache.Cache;
import com.sivaprasad.cacheforge.cache.InMemoryCache;
import com.sivaprasad.cacheforge.config.CacheConfig;

/**
 * CacheForge Phase 4 Verification Application testing TTL & Key Expiration.
 */
public class CacheForgeApplication {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CacheForge Engine - Phase 4 Verification");
        System.out.println("==========================================");

        CacheConfig config = new CacheConfig(100, true);
        Cache<String, String> cache = new InMemoryCache<>(config);

        try {
            // 1. SET with TTL
            System.out.println("\n[1] Storing entries with and without TTL...");
            cache.put("session:101", "SIVA_AUTH_TOKEN", 2); // 2 seconds TTL
            cache.put("config:theme", "dark");             // No TTL (-1)

            System.out.println("GET session:101 -> " + cache.get("session:101"));
            System.out.println("TTL session:101 -> " + cache.getTtl("session:101") + " seconds");
            System.out.println("TTL config:theme -> " + cache.getTtl("config:theme") + " (No expiration)");

            // 2. EXPIRE command on existing key
            System.out.println("\n[2] Applying EXPIRE command to 'config:theme' for 3 seconds...");
            boolean expireStatus = cache.expire("config:theme", 3);
            System.out.println("EXPIRE config:theme 3 -> Success: " + expireStatus);
            System.out.println("TTL config:theme      -> " + cache.getTtl("config:theme") + " seconds");

            // 3. Wait for TTL expiration (Sleep 2.2 seconds)
            System.out.println("\n[3] Sleeping for 2.2 seconds to allow 'session:101' to expire...");
            Thread.sleep(2200);

            // 4. Verify Expiration
            System.out.println("\n[4] Verifying Expiration Outcomes...");
            System.out.println("GET session:101 -> " + cache.get("session:101") + " (Expected: null - EXPIRED!)");
            System.out.println("TTL session:101 -> " + cache.getTtl("session:101") + " (Expected: -2 - Expired/Not Found)");
            System.out.println("GET config:theme -> " + cache.get("config:theme") + " (Still valid)");
            System.out.println("TTL config:theme -> " + cache.getTtl("config:theme") + " seconds remaining");

            // 5. Expiration Statistics
            System.out.println("\n[5] Cache Statistics:");
            System.out.println(cache.getStatistics());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Execution interrupted: " + e.getMessage());
        } finally {
            // Clean up background expiration threads
            cache.shutdown();
            System.out.println("\n[6] Expiration Manager background executor cleanly shut down.");
        }

        System.out.println("\n==========================================");
        System.out.println(" Phase 4 Verification Completed Successfully!");
        System.out.println("==========================================");
    }
}
