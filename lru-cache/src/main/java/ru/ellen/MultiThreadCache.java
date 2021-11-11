package ru.ellen;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiThreadCache<K, V> implements LruCache<K, V> {
    private final int capacity;
    private ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private ConcurrentLinkedQueue<K> timeQueue = new ConcurrentLinkedQueue<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();
    private Lock readLock = lock.readLock();

    public MultiThreadCache(int capacity) {
        this.capacity = capacity;
    }

    public Optional<V> get(K key) {
        readLock.lock();
        try {
            if (cache.containsKey(key)) {
                timeQueue.remove(key);
                timeQueue.add(key);
                return Optional.of(cache.get(key));
            }
            return Optional.empty();
        } finally {
            readLock.unlock();
        }
    }

    public void put(K key, V value) {
        writeLock.lock();
        try {
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
        } finally {
            writeLock.unlock();
        }
    }

    public Set<K> getCache() {
        return cache.keySet();
    }
}
