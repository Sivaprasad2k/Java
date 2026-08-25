package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.cache.InMemoryCache;
import com.sivaprasad.cacheforge.config.CacheConfig;
import com.sivaprasad.cacheforge.event.listener.AuditEventListener;
import com.sivaprasad.cacheforge.event.listener.LoggingEventListener;

/**
 * CacheForge Phase 6 In-Process Event System Verification Application.
 */
public class CacheForgeApplication {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CacheForge Engine - Phase 6 Verification");
        System.out.println("==========================================");

        CacheConfig config = new CacheConfig(3, true);
        InMemoryCache<String, String> cache = new InMemoryCache<>(config);

        // Register Event Listeners
        LoggingEventListener<String, String> loggingListener = new LoggingEventListener<>();
        AuditEventListener<String, String> auditListener = new AuditEventListener<>();

        cache.getEventBus().registerListener(loggingListener);
        cache.getEventBus().registerListener(auditListener);

        System.out.println("\n[1] Registered 2 Event Listeners (Logging & Audit).");
        System.out.println("Executing Cache Operations to trigger EventBus...\n");

        // 1. ENTRY_CREATED & ENTRY_UPDATED
        cache.put("user:101", "Siva");
        cache.put("user:101", "Sivaprasad");

        // 2. Additional Entries
        cache.put("user:102", "Prasad");
        cache.put("user:103", "Avis");

        // 3. ENTRY_EVICTED (Exceeding maxCapacity 3)
        cache.put("user:104", "Dasarp");

        // 4. ENTRY_DELETED
        cache.remove("user:104");

        // 5. ENTRY_EXPIRED
        cache.put("session:temp", "EXPIRE_VAL", 1); // 1 sec TTL
        try {
            Thread.sleep(1200);
            cache.get("session:temp"); // Triggers lazy expiration
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 6. Audit Trail Summary
        System.out.println("\n[2] Audit Event Listener Summary:");
        System.out.println("Total Events Recorded in Audit Trail: " + auditListener.getEventCount());
        auditListener.getAuditLog().forEach(event ->
            System.out.println("  -> Audit Record: " + event.getType() + " for Key [" + event.getKey() + "]")
        );

        cache.shutdown();

        System.out.println("\n==========================================");
        System.out.println(" Phase 6 Verification Completed Successfully!");
        System.out.println("==========================================");
    }
}
