package ru.ellen;


import java.util.*;

public class SingleThreadCache implements LruCache {
    private final int capacity;
    private Map<String, Object> cache = new HashMap<>();
    private LinkedList<String> timeQueue = new LinkedList<>();

    public SingleThreadCache(int capacity) {
        this.capacity = capacity;
    }

    public Object get(String key) {
        if (cache.containsKey(key)) {
            timeQueue.remove(key);
            timeQueue.add(key);
            return cache.get(key);
        }
        return null;
    }

    public void put(String key, Object value) {
        if (cache.containsKey(key)) {
            timeQueue.remove(key);
            cache.put(key, value);
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

    public Set<String> getCache() {
        return cache.keySet();
    }
}
