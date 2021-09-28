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

    public Object get(Object o) {
        return cache.get(o);
    }

    public void put(String o) {
        cache.put(o, super.algorithm(o));
    }

    @Override
    public Object algorithm(String o) {

        // cache contains key
        if (cache.keySet().contains(o)) {
            // put element to the tail
            timeQueue.remove(o);
            timeQueue.add(o);

            return get(o);
        }

        Object currentResult = super.algorithm(o);

        if (cache.size() == capacity) {
            Object lastUsed = timeQueue.poll();
            cache.remove(lastUsed);
        }

        timeQueue.offer(o);
        cache.put(o, currentResult);

        return o;
    }

    @Override
    public Set<String> getCache() {
        return cache.keySet();
    }
}
