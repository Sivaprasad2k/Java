package com.sivaprasad.cacheforge.expiration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Manages active background key expiration using a shared ScheduledExecutorService daemon thread.
 */
public class ExpirationManager {

    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> cleanupFuture;

    public ExpirationManager() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "cacheforge-expiration-worker");
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * Starts periodic active expiration cleanup.
     *
     * @param cleanupTask Runnable task executing expiration logic across cache entries.
     * @param intervalMs Frequency interval in milliseconds.
     */
    public synchronized void startActiveCleanup(Runnable cleanupTask, long intervalMs) {
        if (cleanupFuture != null && !cleanupFuture.isCancelled()) {
            cleanupFuture.cancel(false);
        }
        cleanupFuture = scheduler.scheduleAtFixedRate(cleanupTask, intervalMs, intervalMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops active background expiration tasks and shuts down the executor thread pool.
     */
    public synchronized void shutdown() {
        if (cleanupFuture != null) {
            cleanupFuture.cancel(true);
        }
        scheduler.shutdownNow();
    }
}
