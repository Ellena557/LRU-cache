package ru.ellen;

import java.util.Set;

public abstract class LruCache<T> {
    public abstract Object get(Object o);

    public abstract Set<String> getCache();

    public Object algorithm(String o) {
        return o.length() * 5 + 1;
    }
}
