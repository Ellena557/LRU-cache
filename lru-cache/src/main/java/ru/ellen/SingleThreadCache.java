package ru.ellen;


import java.util.*;

public class SingleThreadCache extends LruCache {
    private final int capacity;
    private Map<String, Object> cache;
    private LinkedList<String> timeQueue;

    public SingleThreadCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.timeQueue = new LinkedList<>();
    }

    public Object get(String key) {
        return cache.getOrDefault(key, null);
    }

    public void put(String key, Object value) {
        if (cache.containsKey(key)) {
            timeQueue.remove(key);
            timeQueue.add(key);
        } else {
            if (cache.size() == capacity) {
                String lastUsed = timeQueue.poll();
                cache.remove(lastUsed);
            }

            timeQueue.add(key);
            cache.put(key, value);
        }
    }

    @Override
    public Object algorithm(String key) {

        // cache contains key
        if (cache.keySet().contains(key)) {
            // put element to the tail
            timeQueue.remove(key);
            timeQueue.add(key);
            return get(key);
        }

        Object currentResult = super.algorithm(key);

        if (cache.size() == capacity) {
            String lastUsed = timeQueue.poll();
            cache.remove(lastUsed);
        }

        timeQueue.add(key);
        cache.put(key, currentResult);

        return key;
    }

    @Override
    public Set<String> getCache() {
        return cache.keySet();
    }
}
