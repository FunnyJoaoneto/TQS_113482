package org.tqs.deti.ua.homework.Cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.tqs.deti.ua.homework.cache.InMemoryCache;
import org.tqs.deti.ua.homework.cache.CacheStatistics;

import java.util.List;

public class InMemoryCacheTest {

    private InMemoryCache<String, String> cache;

    @BeforeEach
    public void setUp() {
        // Initialize the cache before each test
        cache = new InMemoryCache<>();
    }

    @Test
    public void testPutAndGet() {
        cache.put("key1", "value1");
        String value = cache.get("key1");
        assertNotNull(value, "The value should not be null.");
        assertEquals("value1", value, "The value should be 'value1'.");
    }

    @Test
    public void testPutWithTTL() {
        cache.put("key2", "value2", 2); // 2 seconds TTL
        String value = cache.get("key2");
        assertNotNull(value, "The value should not be null.");
        assertEquals("value2", value, "The value should be 'value2'.");

        // Wait for TTL to expire
        try {
            Thread.sleep(3000); // Sleep for 3 seconds (TTL has expired)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Now the cache should return null because the item is expired
        assertNull(cache.get("key2"), "The value should be null after TTL expires.");
    }

    @Test
    public void testEvict() {
        cache.put("key3", "value3");
        cache.evict("key3");
        assertNull(cache.get("key3"), "The value should be null after eviction.");
    }

    @Test
    public void testEvictAll() {
        cache.put("key4", "value4");
        cache.put("key5", "value5");
        cache.evictAll();
        assertNull(cache.get("key4"), "The value should be null after evicting all.");
        assertNull(cache.get("key5"), "The value should be null after evicting all.");
    }

    @Test
    public void testContainsKey() {
        cache.put("key6", "value6");
        assertTrue(cache.containsKey("key6"), "The cache should contain 'key6'.");
        assertFalse(cache.containsKey("key7"), "The cache should not contain 'key7'.");
    }

    @Test
    public void testGetKeys() {
        cache.put("key8", "value8");
        cache.put("key9", "value9");

        List<String> keys = cache.getKeys();
        assertTrue(keys.contains("key8"), "The key list should contain 'key8'.");
        assertTrue(keys.contains("key9"), "The key list should contain 'key9'.");
    }

    @Test
    public void testCacheStatistics() {
        CacheStatistics stats = new CacheStatistics();

        stats.incrementHits();
        stats.incrementMisses();
        stats.incrementPuts();

        assertEquals(1, stats.getHits(), "Hits should be 1.");
        assertEquals(1, stats.getMisses(), "Misses should be 1.");
        assertEquals(1, stats.getPuts(), "Puts should be 1.");
        assertEquals(2, stats.getRequests(), "Total requests should be 2.");
    }
}
