package com.sivaprasad.cacheforge.benchmark;

import com.sivaprasad.cacheforge.cache.Cache;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Performance benchmarking harness measuring single-threaded vs multi-threaded throughput and latency.
 */
public class CacheBenchmark {

    public static void runBenchmark(Cache<String, String> cache) {
        System.out.println("\n==========================================");
        System.out.println(" CacheForge Engine - Performance Benchmarks");
        System.out.println("==========================================");

        runSingleThreadedBenchmark(cache, 50000);
        runMultiThreadedBenchmark(cache, 10, 5000);

        System.out.println("==========================================");
    }

    private static void runSingleThreadedBenchmark(Cache<String, String> cache, int iterations) {
        System.out.printf("\n[1] Single-Threaded Benchmark (%d PUTs + %d GETs = %d ops)...\n",
                iterations, iterations, iterations * 2);

        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            cache.put("bench:st:" + i, "val:" + i);
            cache.get("bench:st:" + i);
        }
        long durationNs = System.nanoTime() - start;
        double durationMs = durationNs / 1_000_000.0;
        int totalOps = iterations * 2;
        long opsPerSec = (long) (totalOps / (durationMs / 1000.0));
        double avgLatencyUs = (durationNs / 1000.0) / totalOps;

        System.out.printf("  Duration      : %.2f ms\n", durationMs);
        System.out.printf("  Throughput    : %,d ops/sec\n", opsPerSec);
        System.out.printf("  Avg Latency   : %.3f µs/op\n", avgLatencyUs);
    }

    private static void runMultiThreadedBenchmark(Cache<String, String> cache, int threads, int opsPerThread) {
        int totalOps = threads * opsPerThread * 2;
        System.out.printf("\n[2] Multi-Threaded Benchmark (%d Threads x %d Ops = %d total ops)...\n",
                threads, opsPerThread * 2, totalOps);

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        long start = System.nanoTime();
        for (int t = 0; t < threads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    for (int i = 0; i < opsPerThread; i++) {
                        String key = "bench:mt:" + threadId + ":" + i;
                        cache.put(key, "val:" + i);
                        cache.get(key);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
            long durationNs = System.nanoTime() - start;
            double durationMs = durationNs / 1_000_000.0;
            long opsPerSec = (long) (totalOps / (durationMs / 1000.0));
            double avgLatencyUs = (durationNs / 1000.0) / totalOps;

            System.out.printf("  Duration      : %.2f ms\n", durationMs);
            System.out.printf("  Throughput    : %,d ops/sec\n", opsPerSec);
            System.out.printf("  Avg Latency   : %.3f µs/op\n", avgLatencyUs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Benchmark interrupted: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}
