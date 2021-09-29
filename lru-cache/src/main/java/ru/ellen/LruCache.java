package ru.ellen;

import java.util.Set;

public interface LruCache {
    Object get(String key);

    void put(String key, Object value);

    Set<String> getCache();
}
