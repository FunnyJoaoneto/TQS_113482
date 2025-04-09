package org.tqs.deti.ua.homework.cache;

public class CacheStatistics {
    private int hits;
    private int misses;
    private int puts;

    // Constructor
    public CacheStatistics() {
        this.hits = 0;
        this.misses = 0;
        this.puts = 0;
    }

    // Getters
    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public int getPuts() {
        return puts;
    }

    // Setters
    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setMisses(int misses) {
        this.misses = misses;
    }

    public void setPuts(int puts) {
        this.puts = puts;
    }

    // Increment methods
    public void incrementHits() {
        this.hits++;
    }

    public void incrementMisses() {
        this.misses++;
    }

    public void incrementPuts() {
        this.puts++;
    }

    // Get Requests
    public int getRequests() {
        return hits + misses;
    }

    @Override
    public String toString() {
        return "CacheStatistics [hits=" + hits + ", misses=" + misses + ", puts=" + puts + "]";
    }
}
