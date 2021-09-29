package ru.ellen;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiThreadCache implements LruCache {
    private final int capacity;
    private ConcurrentHashMap<String, Object> cache;
    private ConcurrentLinkedQueue<String> timeQueue;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();
    private Lock readLock = lock.readLock();

    public MultiThreadCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>();
        this.timeQueue = new ConcurrentLinkedQueue<>();
    }

    public Object get(String key) {
        readLock.lock();
        try {
            if (cache.containsKey(key)) {
                timeQueue.remove(key);
                timeQueue.add(key);
                return cache.get(key);
            }
            return null;
        } finally {
            readLock.unlock();
        }
    }

    public void put(String key, Object value) {
        writeLock.lock();
        try {
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
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Set<String> getCache() {
        return cache.keySet();
    }
}
