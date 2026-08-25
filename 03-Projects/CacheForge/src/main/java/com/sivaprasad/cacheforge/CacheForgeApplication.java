package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.benchmark.CacheBenchmark;
import com.sivaprasad.cacheforge.cache.InMemoryCache;
import com.sivaprasad.cacheforge.cli.Console;
import com.sivaprasad.cacheforge.config.CacheConfig;

/**
 * CacheForge Main Application Launcher.
 */
public class CacheForgeApplication {

    public static void main(String[] args) {
        CacheConfig config = new CacheConfig(10000, true);
        InMemoryCache<String, String> cache = new InMemoryCache<>(config);

        if (args.length > 0 && "--cli".equalsIgnoreCase(args[0])) {
            try {
                Console.startRepl(cache);
            } finally {
                cache.shutdown();
            }
        } else {
            System.out.println("==========================================");
            System.out.println(" CacheForge Engine - System Verification");
            System.out.println("==========================================");

            // 1. Basic Operations
            cache.put("user:101", "Siva");
            cache.put("user:102", "Prasad");
            cache.put("config:theme", "dark", 120);

            System.out.println("\n[1] Basic Operations Verification:");
            System.out.println("GET user:101      -> " + cache.get("user:101"));
            System.out.println("EXISTS config:theme -> " + cache.containsKey("config:theme"));
            System.out.println("TTL config:theme  -> " + cache.getTtl("config:theme") + "s");

            // 2. Metrics & Telemetry
            System.out.println("\n[2] Operational Metrics:");
            System.out.println(cache.getStatistics());

            // 3. Performance Benchmarks
            CacheBenchmark.runBenchmark(cache);

            cache.shutdown();

            System.out.println("\n==========================================");
            System.out.println(" All Systems Operational! Launch interactive REPL using '--cli'");
            System.out.println("==========================================");
        }
    }
}
