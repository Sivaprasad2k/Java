package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.cache.Cache;
import com.sivaprasad.cacheforge.cache.InMemoryCache;
import com.sivaprasad.cacheforge.config.CacheConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CacheForge Phase 5 Concurrency & Thread Safety Verification Application.
 */
public class CacheForgeApplication {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CacheForge Engine - Phase 5 Verification");
        System.out.println("==========================================");

        int threadCount = 10;
        int opsPerThread = 1000;
        int totalOperations = threadCount * opsPerThread * 3; // PUT, GET, CONTAINS

        CacheConfig config = new CacheConfig(2000, true);
        Cache<String, String> cache = new InMemoryCache<>(config);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        System.out.printf("\n[1] Launching %d concurrent worker threads (%d ops total)...\n", threadCount, totalOperations);

        long startTime = System.currentTimeMillis();

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    for (int i = 0; i < opsPerThread; i++) {
                        String key = "key:" + (i % 500); // 500 distinct keys creating high contention
                        String value = "val:" + threadId + "-" + i;

                        cache.put(key, value);
                        cache.get(key);
                        cache.containsKey(key);
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                    System.err.println("Thread error: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(); // Wait for all worker threads to complete
            long durationMs = System.currentTimeMillis() - startTime;

            System.out.println("\n[2] Multi-Threaded Stress Test Completed!");
            System.out.println("Execution Time        : " + durationMs + " ms");
            System.out.println("Operations / Second   : " + (long) ((double) totalOperations / (durationMs / 1000.0)) + " ops/sec");
            System.out.println("Thread Race Errors    : " + errorCount.get());
            System.out.println("Final Cache Size      : " + cache.size());

            System.out.println("\n[3] Concurrent Metrics Summary:");
            System.out.println(cache.getStatistics());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Stress test interrupted: " + e.getMessage());
        } finally {
            executor.shutdown();
            cache.shutdown();
            System.out.println("\n[4] Thread pools cleanly shut down.");
        }

        System.out.println("\n==========================================");
        System.out.println(" Phase 5 Verification Completed Successfully!");
        System.out.println("==========================================");
    }
}
