package com.sivaprasad.cacheforge.event;

/**
 * Enumerates event types emitted during cache mutations.
 */
public enum EventType {
    ENTRY_CREATED,
    ENTRY_UPDATED,
    ENTRY_DELETED,
    ENTRY_EXPIRED,
    ENTRY_EVICTED
}
