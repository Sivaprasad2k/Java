package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.cache.InMemoryCache;
import com.sivaprasad.cacheforge.config.CacheConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * CacheForge Phase 8 File Persistence & State Recovery Verification Application.
 */
public class CacheForgeApplication {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CacheForge Engine - Phase 8 Verification");
        System.out.println("==========================================");

        CacheConfig config = new CacheConfig(100, true);
        InMemoryCache<String, String> cache = new InMemoryCache<>(config);

        Path snapshotPath = Paths.get("03-Projects/CacheForge/data/cache_snapshot.dat");

        try {
            // 1. Populate initial cache state
            System.out.println("\n[1] Populating cache entries...");
            cache.put("user:101", "Siva");
            cache.put("user:102", "Prasad");
            cache.put("config:theme", "dark", 120);

            System.out.println("Current Cache Size: " + cache.size());

            // 2. Persist Snapshot to disk
            System.out.println("\n[2] Saving snapshot to local file: " + snapshotPath.toAbsolutePath());
            cache.saveSnapshot(snapshotPath);
            System.out.println("Snapshot successfully written to disk.");

            // 3. Display Raw Disk File Contents
            System.out.println("\n[3] Raw Disk File Contents:");
            List<String> fileLines = Files.readAllLines(snapshotPath);
            fileLines.forEach(line -> System.out.println("  " + line));

            // 4. Simulate Application Restart / Memory Clear
            System.out.println("\n[4] Simulating cache memory wipe (CLEAR)...");
            cache.clear();
            System.out.println("Cache Size after clear: " + cache.size());
            System.out.println("GET user:101 -> " + cache.get("user:101") + " (Expected: null)");

            // 5. Reconstruct State from Disk Snapshot
            System.out.println("\n[5] Restoring cache state from disk snapshot...");
            int restoredCount = cache.loadSnapshot(snapshotPath);
            System.out.println("Restored Entries Count: " + restoredCount);
            System.out.println("Reconstructed Cache Size: " + cache.size());

            // 6. Verify Restored Values
            System.out.println("\n[6] Verifying Restored State:");
            System.out.println("GET user:101      -> " + cache.get("user:101"));
            System.out.println("GET user:102      -> " + cache.get("user:102"));
            System.out.println("GET config:theme  -> " + cache.get("config:theme"));
            System.out.println("TTL config:theme  -> " + cache.getTtl("config:theme") + " seconds remaining");

        } catch (IOException e) {
            System.err.println("Persistence I/O error: " + e.getMessage());
        } finally {
            cache.shutdown();
        }

        System.out.println("\n==========================================");
        System.out.println(" Phase 8 Verification Completed Successfully!");
        System.out.println("==========================================");
    }
}
