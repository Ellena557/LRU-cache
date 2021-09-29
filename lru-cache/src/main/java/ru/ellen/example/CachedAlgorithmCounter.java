package ru.ellen.example;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class CachedAlgorithmCounter extends AlgorithmCounter {
    private final int capacity;
    private Map<String, Object> cache = new HashMap<>();
    private LinkedList<String> timeQueue = new LinkedList<>();

    public CachedAlgorithmCounter(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public Set<String> getCache() {
        return cache.keySet();
    }

    @Override
    public Object algorithm(String key) {

        // cache contains key
        if (cache.keySet().contains(key)) {
            // put element to the tail
            timeQueue.remove(key);
            timeQueue.add(key);
            return cache.get(key);
        }

        Object currentResult = super.algorithm(key);

        if (cache.size() == capacity) {
            String lastUsed = timeQueue.poll();
            cache.remove(lastUsed);
        }

        timeQueue.add(key);
        cache.put(key, currentResult);

        return currentResult;
    }
}
