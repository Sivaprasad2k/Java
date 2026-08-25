package com.sivaprasad.cacheforge.config;

/**
 * Encapsulates configuration parameters for a Cache instance.
 */
public class CacheConfig {

    public static final int DEFAULT_MAX_CAPACITY = 1000;

    private final int maxCapacity;
    private final boolean statisticsEnabled;

    public CacheConfig() {
        this(DEFAULT_MAX_CAPACITY, true);
    }

    public CacheConfig(int maxCapacity, boolean statisticsEnabled) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Max capacity must be greater than zero");
        }
        this.maxCapacity = maxCapacity;
        this.statisticsEnabled = statisticsEnabled;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean isStatisticsEnabled() {
        return statisticsEnabled;
    }

    @Override
    public String toString() {
        return "CacheConfig{" +
                "maxCapacity=" + maxCapacity +
                ", statisticsEnabled=" + statisticsEnabled +
                '}';
    }
}
