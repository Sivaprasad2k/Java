package com.sivaprasad.cacheforge.metrics;

/**
 * Encapsulates performance metrics and operational counters for a cache instance.
 */
public class CacheStatistics {

    private long hits;
    private long misses;
    private long puts;
    private long removals;
    private long evictions;

    public CacheStatistics() {
        this.hits = 0;
        this.misses = 0;
        this.puts = 0;
        this.removals = 0;
        this.evictions = 0;
    }

    public void recordHit() {
        hits++;
    }

    public void recordMiss() {
        misses++;
    }

    public void recordPut() {
        puts++;
    }

    public void recordRemoval() {
        removals++;
    }

    public void recordEviction() {
        evictions++;
    }

    public long getHits() {
        return hits;
    }

    public long getMisses() {
        return misses;
    }

    public long getPuts() {
        return puts;
    }

    public long getRemovals() {
        return removals;
    }

    public long getEvictions() {
        return evictions;
    }

    public long getTotalRequests() {
        return hits + misses;
    }

    public double getHitRatio() {
        long total = getTotalRequests();
        if (total == 0) {
            return 0.0;
        }
        return ((double) hits / total) * 100.0;
    }

    @Override
    public String toString() {
        return String.format(
            "CacheStatistics[Hits: %d, Misses: %d, Puts: %d, Removals: %d, Evictions: %d, Hit Ratio: %.2f%%]",
            hits, misses, puts, removals, evictions, getHitRatio()
        );
    }
}
