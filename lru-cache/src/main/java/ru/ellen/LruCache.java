package ru.ellen;

import java.util.Optional;
import java.util.Set;

public interface LruCache<K, V> {
    Optional<V> get(K key);

    void put(K key, V value);

    Set<K> getCache();
}
