package com.sivaprasad.cacheforge.cache;

/**
 * Result wrapper providing explicit feedback for cache operations.
 * Java 17 Generic Record implementation.
 *
 * @param <V> Type of value returned.
 */
public record CacheResult<V>(boolean success, V value, String message) {

    public static <V> CacheResult<V> success(V value) {
        return new CacheResult<>(true, value, "Operation successful");
    }

    public static <V> CacheResult<V> success(V value, String message) {
        return new CacheResult<>(true, value, message);
    }

    public static <V> CacheResult<V> failure(String message) {
        return new CacheResult<>(false, null, message);
    }
}
