package org.tqs.deti.ua.homework.cache;

public class CacheItem<V> {
    private final V value;
    private final int ttl;
    private long expirationTime;

    // Constructor
    public CacheItem(V value, int ttl) {
        this.value = value;
        this.ttl = ttl;
        this.expirationTime = System.currentTimeMillis() + ttl * 1000L;
    }

    // Getters
    public V getValue() {
        return value;
    }

    public int getTtl() {
        return ttl;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    // Check if the cache item is expired
    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime;
    }

    @Override
    public String toString() {
        return "{value: " + value + ", ttl: " + ttl + ", expirationTime: " + expirationTime + "}";
    }
}
