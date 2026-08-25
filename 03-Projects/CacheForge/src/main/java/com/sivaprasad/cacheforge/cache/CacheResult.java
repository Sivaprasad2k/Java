package com.sivaprasad.cacheforge.cache;

/**
 * Result wrapper providing explicit feedback for cache operations.
 *
 * @param <V> Type of value returned.
 */
public class CacheResult<V> {

    private final boolean success;
    private final V value;
    private final String message;

    private CacheResult(boolean success, V value, String message) {
        this.success = success;
        this.value = value;
        this.message = message;
    }

    public static <V> CacheResult<V> success(V value) {
        return new CacheResult<>(true, value, "Operation successful");
    }

    public static <V> CacheResult<V> success(V value, String message) {
        return new CacheResult<>(true, value, message);
    }

    public static <V> CacheResult<V> failure(String message) {
        return new CacheResult<>(false, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public V getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CacheResult{" +
                "success=" + success +
                ", value=" + value +
                ", message='" + message + '\'' +
                '}';
    }
}
