package ru.ellen;


import java.util.*;

public class SingleThreadCache<K, V> implements LruCache<K, V> {
    private final int capacity;
    private Map<K, V> cache = new HashMap<>();
    private LinkedList<K> timeQueue = new LinkedList<>();

    public SingleThreadCache(int capacity) {
        this.capacity = capacity;
    }

    public Optional<V> get(K key) {
        if (cache.containsKey(key)) {
            timeQueue.remove(key);
            timeQueue.add(key);
            return Optional.of(cache.get(key));
        }
        return Optional.empty();
    }

    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            timeQueue.remove(key);
            cache.put(key, value);
            timeQueue.add(key);
        } else {
            if (cache.size() == capacity) {
                K lastUsed = timeQueue.poll();
                cache.remove(lastUsed);
            }

            timeQueue.add(key);
            cache.put(key, value);
        }
    }

    public Set<K> getCache() {
        return cache.keySet();
    }
}
