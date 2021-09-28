package ru.ellen;

import java.util.Set;

public abstract class LruCache {
    public abstract Object get(String key);

    public abstract void put(String key, Object value);

    public abstract Set<String> getCache();

    public Object algorithm(String key) {
        return key.length() * 5 + 1;
    }
}
