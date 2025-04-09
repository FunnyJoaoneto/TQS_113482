package org.tqs.deti.ua.homework.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.List;

public class InMemoryCache<K, V> {

    private final Map<K, CacheItem<V>> cache;
    private static final int DEFAULT_TTL = 60;

    // Constructor
    public InMemoryCache() {
        this.cache = new ConcurrentHashMap<>();
        new Thread(new CacheEvict()).start();
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public V get(K key) {
        CacheItem<V> item = cache.get(key);
        if (item == null || item.isExpired()) {
            return null;
        }
        return item.getValue();
    }

    public void put(K key, V value) {
        cache.put(key, new CacheItem<>(value, DEFAULT_TTL));
    }

    public void put(K key, V value, int ttl) {
        cache.put(key, new CacheItem<>(value, ttl));
    }

    public void evict(K key) {
        cache.remove(key);
    }

    public void evictAll() {
        cache.clear();
    }

    public List<K> getKeys() {
        return List.copyOf(cache.keySet());
    }

    @Override
    public String toString() {
        return cache.toString();
    }

    private class CacheEvict implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // evict cache
                for (Map.Entry<K, CacheItem<V>> entry : cache.entrySet()) {
                    if (entry.getValue().isExpired()) {
                        cache.remove(entry.getKey());
                    }
                }
            }
        }
    }
}
