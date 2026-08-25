package com.sivaprasad.cacheforge.cli;

import com.sivaprasad.cacheforge.analytics.CacheAnalytics;
import com.sivaprasad.cacheforge.benchmark.CacheBenchmark;
import com.sivaprasad.cacheforge.cache.InMemoryCache;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Interactive REPL Console Interface for CacheForge.
 * Java 17 implementation utilizing modern switch expressions and text blocks.
 */
public class Console {

    private static final Path DEFAULT_SNAPSHOT_PATH = Paths.get("03-Projects/CacheForge/data/cache_snapshot.dat");

    public static void startRepl(InMemoryCache<String, String> cache) {
        var scanner = new Scanner(System.in);
        System.out.println("""
            ==================================================
               Welcome to CacheForge Interactive REPL Shell
               Type 'HELP' for available commands or 'EXIT' to quit
            ==================================================""");

        boolean running = true;
        while (running) {
            System.out.print("\nCACHEFORGE> ");
            if (!scanner.hasNextLine()) break;
            var line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            var tokens = line.split("\\s+");
            var command = tokens[0].toUpperCase();

            switch (command) {
                case "SET" -> {
                    if (tokens.length < 3) {
                        System.out.println("ERROR: Usage -> SET <key> <value> [ttlSeconds]");
                    } else if (tokens.length >= 4) {
                        try {
                            long ttl = Long.parseLong(tokens[3]);
                            cache.put(tokens[1], tokens[2], ttl);
                            System.out.println("OK (TTL: " + ttl + "s)");
                        } catch (NumberFormatException e) {
                            System.out.println("ERROR: Invalid TTL number format");
                        }
                    } else {
                        cache.put(tokens[1], tokens[2]);
                        System.out.println("OK");
                    }
                }
                case "GET" -> {
                    if (tokens.length < 2) {
                        System.out.println("ERROR: Usage -> GET <key>");
                    } else {
                        var val = cache.get(tokens[1]);
                        System.out.println(val != null ? val : "(nil)");
                    }
                }
                case "DELETE", "DEL" -> {
                    if (tokens.length < 2) {
                        System.out.println("ERROR: Usage -> DELETE <key>");
                    } else {
                        boolean removed = cache.remove(tokens[1]);
                        System.out.println(removed ? "OK (1 entry deleted)" : "(integer) 0");
                    }
                }
                case "EXISTS" -> {
                    if (tokens.length < 2) {
                        System.out.println("ERROR: Usage -> EXISTS <key>");
                    } else {
                        boolean exists = cache.containsKey(tokens[1]);
                        System.out.println(exists ? "(integer) 1" : "(integer) 0");
                    }
                }
                case "EXPIRE" -> {
                    if (tokens.length < 3) {
                        System.out.println("ERROR: Usage -> EXPIRE <key> <seconds>");
                    } else {
                        try {
                            long seconds = Long.parseLong(tokens[2]);
                            boolean ok = cache.expire(tokens[1], seconds);
                            System.out.println(ok ? "(integer) 1" : "(integer) 0");
                        } catch (NumberFormatException e) {
                            System.out.println("ERROR: Invalid seconds value");
                        }
                    }
                }
                case "TTL" -> {
                    if (tokens.length < 2) {
                        System.out.println("ERROR: Usage -> TTL <key>");
                    } else {
                        long ttl = cache.getTtl(tokens[1]);
                        System.out.println("(integer) " + ttl);
                    }
                }
                case "SIZE" -> System.out.println("(integer) " + cache.size());
                case "STATS" -> System.out.println(cache.getStatistics());
                case "ANALYTICS" -> {
                    var analytics = cache.getAnalytics();
                    System.out.println("--- Cache Telemetry Analytics ---");
                    System.out.println("Top 3 Keys       : " + analytics.getTopAccessedKeys(3));
                    System.out.printf ("Average TTL      : %.2f seconds\n", analytics.getAverageTtlSeconds());
                    System.out.println("Total Read Ops   : " + analytics.getTotalAccesses());
                    System.out.println("Namespaces       : ");
                    Map<String, List<String>> nsMap = analytics.getKeysGroupedByNamespace();
                    nsMap.forEach((ns, keys) -> System.out.println("  * [" + ns + "]: " + keys));
                }
                case "SAVE" -> {
                    try {
                        cache.saveSnapshot(DEFAULT_SNAPSHOT_PATH);
                        System.out.println("OK (Snapshot saved to " + DEFAULT_SNAPSHOT_PATH + ")");
                    } catch (IOException e) {
                        System.out.println("ERROR: Persistence write failed -> " + e.getMessage());
                    }
                }
                case "LOAD" -> {
                    try {
                        int count = cache.loadSnapshot(DEFAULT_SNAPSHOT_PATH);
                        System.out.println("OK (Restored " + count + " entries)");
                    } catch (IOException e) {
                        System.out.println("ERROR: Persistence load failed -> " + e.getMessage());
                    }
                }
                case "CLEAR" -> {
                    cache.clear();
                    System.out.println("OK (Cache cleared)");
                }
                case "BENCHMARK" -> CacheBenchmark.runBenchmark(cache);
                case "HELP" -> printHelp();
                case "EXIT", "QUIT" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("ERROR: Unknown command '" + command + "'. Type HELP for command list.");
            }
        }
    }

    private static void printHelp() {
        System.out.println("""
            Supported CacheForge Commands:
              SET <key> <val> [ttl]  : Stores key-value pair with optional TTL in seconds
              GET <key>              : Retrieves value associated with key
              DELETE <key>           : Removes key from cache
              EXISTS <key>           : Checks if key exists and is non-expired
              EXPIRE <key> <seconds> : Configures TTL expiration on existing key
              TTL <key>              : Returns remaining TTL (-1: none, -2: non-existent)
              SIZE                   : Returns current entry count
              STATS                  : Displays hits, misses, hit ratio metrics
              ANALYTICS              : Runs Java Streams telemetry queries
              SAVE                   : Persists snapshot to local disk
              LOAD                   : Restores snapshot from local disk
              CLEAR                  : Removes all entries
              BENCHMARK              : Executes multi-threaded throughput tests
              HELP                   : Displays this command summary
              EXIT                   : Terminates REPL session""");
    }
}
