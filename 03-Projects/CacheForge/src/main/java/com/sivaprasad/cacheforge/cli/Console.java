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
 */
public class Console {

    private static final Path DEFAULT_SNAPSHOT_PATH = Paths.get("03-Projects/CacheForge/data/cache_snapshot.dat");

    public static void startRepl(InMemoryCache<String, String> cache) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================================================");
        System.out.println("   Welcome to CacheForge Interactive REPL Shell");
        System.out.println("   Type 'HELP' for available commands or 'EXIT' to quit");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            System.out.print("\nCACHEFORGE> ");
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] tokens = line.split("\\s+");
            String command = tokens[0].toUpperCase();

            switch (command) {
                case "SET":
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
                    break;

                case "GET":
                    if (tokens.length < 2) {
                        System.out.println("ERROR: Usage -> GET <key>");
                    } else {
                        String val = cache.get(tokens[1]);
                        System.out.println(val != null ? val : "(nil)");
                    }
                    break;

                case "DELETE":
                case "DEL":
                    if (tokens.length < 2) {
                        System.out.println("ERROR: Usage -> DELETE <key>");
                    } else {
                        boolean removed = cache.remove(tokens[1]);
                        System.out.println(removed ? "OK (1 entry deleted)" : "(integer) 0");
                    }
                    break;

                case "EXISTS":
                    if (tokens.length < 2) {
                        System.out.println("ERROR: Usage -> EXISTS <key>");
                    } else {
                        boolean exists = cache.containsKey(tokens[1]);
                        System.out.println(exists ? "(integer) 1" : "(integer) 0");
                    }
                    break;

                case "EXPIRE":
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
                    break;

                case "TTL":
                    if (tokens.length < 2) {
                        System.out.println("ERROR: Usage -> TTL <key>");
                    } else {
                        long ttl = cache.getTtl(tokens[1]);
                        System.out.println("(integer) " + ttl);
                    }
                    break;

                case "SIZE":
                    System.out.println("(integer) " + cache.size());
                    break;

                case "STATS":
                    System.out.println(cache.getStatistics());
                    break;

                case "ANALYTICS":
                    CacheAnalytics<String, String> analytics = cache.getAnalytics();
                    System.out.println("--- Cache Telemetry Analytics ---");
                    System.out.println("Top 3 Keys       : " + analytics.getTopAccessedKeys(3));
                    System.out.printf ("Average TTL      : %.2f seconds\n", analytics.getAverageTtlSeconds());
                    System.out.println("Total Read Ops   : " + analytics.getTotalAccesses());
                    System.out.println("Namespaces       : ");
                    Map<String, List<String>> nsMap = analytics.getKeysGroupedByNamespace();
                    nsMap.forEach((ns, keys) -> System.out.println("  * [" + ns + "]: " + keys));
                    break;

                case "SAVE":
                    try {
                        cache.saveSnapshot(DEFAULT_SNAPSHOT_PATH);
                        System.out.println("OK (Snapshot saved to " + DEFAULT_SNAPSHOT_PATH + ")");
                    } catch (IOException e) {
                        System.out.println("ERROR: Persistence write failed -> " + e.getMessage());
                    }
                    break;

                case "LOAD":
                    try {
                        int count = cache.loadSnapshot(DEFAULT_SNAPSHOT_PATH);
                        System.out.println("OK (Restored " + count + " entries)");
                    } catch (IOException e) {
                        System.out.println("ERROR: Persistence load failed -> " + e.getMessage());
                    }
                    break;

                case "CLEAR":
                    cache.clear();
                    System.out.println("OK (Cache cleared)");
                    break;

                case "BENCHMARK":
                    CacheBenchmark.runBenchmark(cache);
                    break;

                case "HELP":
                    printHelp();
                    break;

                case "EXIT":
                case "QUIT":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("ERROR: Unknown command '" + command + "'. Type HELP for command list.");
            }
        }
    }

    private static void printHelp() {
        System.out.println("\nSupported CacheForge Commands:");
        System.out.println("  SET <key> <val> [ttl]  : Stores key-value pair with optional TTL in seconds");
        System.out.println("  GET <key>              : Retrieves value associated with key");
        System.out.println("  DELETE <key>           : Removes key from cache");
        System.out.println("  EXISTS <key>           : Checks if key exists and is non-expired");
        System.out.println("  EXPIRE <key> <seconds> : Configures TTL expiration on existing key");
        System.out.println("  TTL <key>              : Returns remaining TTL (-1: none, -2: non-existent)");
        System.out.println("  SIZE                   : Returns current entry count");
        System.out.println("  STATS                  : Displays hits, misses, hit ratio metrics");
        System.out.println("  ANALYTICS              : Runs Java Streams telemetry queries");
        System.out.println("  SAVE                   : Persists snapshot to local disk");
        System.out.println("  LOAD                   : Restores snapshot from local disk");
        System.out.println("  CLEAR                  : Removes all entries");
        System.out.println("  BENCHMARK              : Executes multi-threaded throughput tests");
        System.out.println("  HELP                   : Displays this command summary");
        System.out.println("  EXIT                   : Terminates REPL session");
    }
}
