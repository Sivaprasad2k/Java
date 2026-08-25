package com.sivaprasad.cacheforge;

import com.sivaprasad.cacheforge.cache.Cache;
import com.sivaprasad.cacheforge.cache.InMemoryCache;

/**
 * CacheForge Phase 2 Verification Application demonstrating Generics and Domain Objects.
 */
public class CacheForgeApplication {

    // Simple custom User class for generic cache demonstration
    public static class User {
        private final int id;
        private final String name;
        private final String role;

        public User(int id, String name, String role) {
            this.id = id;
            this.name = name;
            this.role = role;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getRole() { return role; }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "', role='" + role + "'}";
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println(" CacheForge Engine - Phase 2 Verification");
        System.out.println("==========================================");

        // 1. Generic String-to-String Cache
        System.out.println("\n[1] Testing Generic Cache<String, String>...");
        Cache<String, String> sessionCache = new InMemoryCache<>();
        sessionCache.put("session:101", "TOKEN_ABC_123");
        sessionCache.put("session:102", "TOKEN_XYZ_789");

        System.out.println("GET session:101 -> " + sessionCache.get("session:101"));
        System.out.println("GET session:999 -> " + sessionCache.get("session:999") + " (Miss)");

        System.out.println("Session Cache Metrics: " + sessionCache.getStatistics());

        // 2. Generic Integer-to-User Object Cache
        System.out.println("\n[2] Testing Generic Cache<Integer, User>...");
        Cache<Integer, User> userCache = new InMemoryCache<>();

        User u1 = new User(1, "Siva", "Developer");
        User u2 = new User(2, "Prasad", "Architect");

        userCache.put(u1.getId(), u1);
        userCache.put(u2.getId(), u2);

        System.out.println("GET User ID 1 -> " + userCache.get(1));
        System.out.println("GET User ID 2 -> " + userCache.get(2));
        System.out.println("GET User ID 1 -> " + userCache.get(1) + " (Hit)");
        System.out.println("GET User ID 9 -> " + userCache.get(9) + " (Miss)");

        System.out.println("User Cache Metrics    : " + userCache.getStatistics());
        System.out.println("User Cache Hit Ratio  : " + String.format("%.2f%%", userCache.getStatistics().getHitRatio()));

        System.out.println("\n==========================================");
        System.out.println(" Phase 2 Verification Completed Successfully!");
        System.out.println("==========================================");
    }
}
