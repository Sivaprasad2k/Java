package com.sivaprasad.cacheforge.persistence;

import com.sivaprasad.cacheforge.cache.Cache;
import com.sivaprasad.cacheforge.cache.CacheEntry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Manages local file persistence and state recovery for CacheForge instances.
 * Implemented using pure Java NIO File APIs (Path, Files, BufferedReader, BufferedWriter).
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class PersistenceManager<K, V> {

    private static final String HEADER = "# CacheForge Snapshot v1\n# KEY|VALUE|EXPIRE_AT_TIMESTAMP";

    /**
     * Serializes active, non-expired cache entries to a local snapshot file.
     */
    public void saveSnapshot(Path filePath, Map<K, CacheEntry<V>> storage) throws IOException {
        if (filePath == null || storage == null) {
            throw new IllegalArgumentException("FilePath and Storage cannot be null");
        }

        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();

            for (Map.Entry<K, CacheEntry<V>> mapEntry : storage.entrySet()) {
                CacheEntry<V> entry = mapEntry.getValue();
                if (entry != null && !entry.isExpired()) {
                    String line = String.format("%s|%s|%d",
                            mapEntry.getKey(),
                            entry.getValue(),
                            entry.getExpireAtTimestamp());
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    /**
     * Reads a snapshot file and reconstructs active valid entries into the target cache.
     *
     * @return Total count of valid restored cache entries.
     */
    @SuppressWarnings("unchecked")
    public int loadSnapshot(Path filePath, Cache<K, V> cache) throws IOException {
        if (filePath == null || cache == null || !Files.exists(filePath)) {
            return 0;
        }

        int restoredCount = 0;
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip comments and empty lines
                }

                String[] parts = line.split("\\|", 3);
                if (parts.length < 3) {
                    continue; // Skip malformed lines
                }

                K key = (K) parts[0];
                V value = (V) parts[1];
                long expireAtTimestamp = Long.parseLong(parts[2]);

                if (expireAtTimestamp != -1) {
                    long remainingMs = expireAtTimestamp - System.currentTimeMillis();
                    if (remainingMs <= 0) {
                        continue; // Skip expired entry from snapshot
                    }
                    long remainingTtlSeconds = (remainingMs + 999) / 1000;
                    cache.put(key, value, remainingTtlSeconds);
                } else {
                    cache.put(key, value);
                }
                restoredCount++;
            }
        }
        return restoredCount;
    }
}
