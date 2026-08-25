package com.sivaprasad.cacheforge.metrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Encapsulates performance metrics and operational counters for a cache instance.
 * Thread-safe implementation utilizing AtomicLong CAS (Compare-And-Swap) counters.
 */
public class CacheStatistics {

    private final AtomicLong hits;
    private final AtomicLong misses;
    private final AtomicLong puts;
    private final AtomicLong removals;
    private final AtomicLong evictions;
    private final AtomicLong expirations;

    public CacheStatistics() {
        this.hits = new AtomicLong(0);
        this.misses = new AtomicLong(0);
        this.puts = new AtomicLong(0);
        this.removals = new AtomicLong(0);
        this.evictions = new AtomicLong(0);
        this.expirations = new AtomicLong(0);
    }

    public void recordHit() {
        hits.incrementAndGet();
    }

    public void recordMiss() {
        misses.incrementAndGet();
    }

    public void recordPut() {
        puts.incrementAndGet();
    }

    public void recordRemoval() {
        removals.incrementAndGet();
    }

    public void recordEviction() {
        evictions.incrementAndGet();
    }

    public void recordExpiration() {
        expirations.incrementAndGet();
    }

    public long getHits() {
        return hits.get();
    }

    public long getMisses() {
        return misses.get();
    }

    public long getPuts() {
        return puts.get();
    }

    public long getRemovals() {
        return removals.get();
    }

    public long getEvictions() {
        return evictions.get();
    }

    public long getExpirations() {
        return expirations.get();
    }

    public long getTotalRequests() {
        return hits.get() + misses.get();
    }

    public double getHitRatio() {
        long total = getTotalRequests();
        if (total == 0) {
            return 0.0;
        }
        return ((double) hits.get() / total) * 100.0;
    }

    @Override
    public String toString() {
        return String.format(
            "CacheStatistics[Hits: %d, Misses: %d, Puts: %d, Removals: %d, Evictions: %d, Expirations: %d, Hit Ratio: %.2f%%]",
            hits.get(), misses.get(), puts.get(), removals.get(), evictions.get(), expirations.get(), getHitRatio()
        );
    }
}
