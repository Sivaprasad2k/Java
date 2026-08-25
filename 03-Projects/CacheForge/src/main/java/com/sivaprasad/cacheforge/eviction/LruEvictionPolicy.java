package com.sivaprasad.cacheforge.eviction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Least Recently Used (LRU) Eviction Policy implemented using a custom Doubly Linked List
 * combined with a HashMap lookup table. Thread-safe implementation guarded by a ReentrantLock.
 *
 * Guarantees O(1) time complexity for access, insertion, deletion, and eviction operations.
 *
 * @param <K> Type of key tracked.
 */
public final class LruEvictionPolicy<K> implements EvictionPolicy<K> {

    private static class Node<K> {
        final K key;
        Node<K> prev;
        Node<K> next;

        Node(K key) {
            this.key = key;
        }
    }

    private final Map<K, Node<K>> nodeMap;
    private final Node<K> head;
    private final Node<K> tail;
    private final ReentrantLock lock;

    public LruEvictionPolicy() {
        this.nodeMap = new HashMap<>();
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
        this.head.next = tail;
        this.tail.prev = head;
        this.lock = new ReentrantLock();
    }

    @Override
    public void keyAccessed(K key) {
        if (key == null) return;
        lock.lock();
        try {
            Node<K> node = nodeMap.get(key);
            if (node != null) {
                moveToHead(node);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void keyInserted(K key) {
        if (key == null) return;
        lock.lock();
        try {
            Node<K> existingNode = nodeMap.get(key);
            if (existingNode != null) {
                moveToHead(existingNode);
            } else {
                Node<K> newNode = new Node<>(key);
                nodeMap.put(key, newNode);
                addNodeToHead(newNode);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void keyRemoved(K key) {
        if (key == null) return;
        lock.lock();
        try {
            Node<K> node = nodeMap.remove(key);
            if (node != null) {
                removeNode(node);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public K evictKey() {
        lock.lock();
        try {
            if (tail.prev == head) {
                return null; // Empty policy tracking
            }
            Node<K> lruNode = tail.prev;
            removeNode(lruNode);
            nodeMap.remove(lruNode.key);
            return lruNode.key;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            nodeMap.clear();
            head.next = tail;
            tail.prev = head;
        } finally {
            lock.unlock();
        }
    }

    // Helper method to add node directly after head (MRU position) - O(1)
    private void addNodeToHead(Node<K> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    // Helper method to unlink node from Doubly Linked List - O(1)
    private void removeNode(Node<K> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Helper method to move existing node to head (MRU position) - O(1)
    private void moveToHead(Node<K> node) {
        removeNode(node);
        addNodeToHead(node);
    }
}
