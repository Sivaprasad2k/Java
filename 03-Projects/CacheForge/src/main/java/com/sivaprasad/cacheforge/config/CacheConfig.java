package com.sivaprasad.cacheforge.config;

/**
 * Encapsulates configuration parameters for a Cache instance.
 * Java 17 Record implementation.
 */
public record CacheConfig(int maxCapacity, boolean statisticsEnabled) {

    public static final int DEFAULT_MAX_CAPACITY = 1000;

    public CacheConfig {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Max capacity must be greater than zero");
        }
    }

    public CacheConfig() {
        this(DEFAULT_MAX_CAPACITY, true);
    }

    public int getMaxCapacity() {
        return maxCapacity();
    }

    public boolean isStatisticsEnabled() {
        return statisticsEnabled();
    }
}
