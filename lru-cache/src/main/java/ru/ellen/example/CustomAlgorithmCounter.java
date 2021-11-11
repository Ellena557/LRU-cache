package ru.ellen.example;

import ru.ellen.LruCache;
import ru.ellen.SingleThreadCache;

import java.util.Set;

public class CustomAlgorithmCounter extends AlgorithmCounter {
    private final LruCache lruCache;

    public CustomAlgorithmCounter(int capacity) {
        this.lruCache = new SingleThreadCache(capacity);
    }

    @Override
    public Set<String> getCache() {
        return lruCache.getCache();
    }

    @Override
    public Object calculate(String key) {

        Object currentResult = lruCache.get(key);

        if (currentResult != null) {
            return currentResult;
        } else {
            currentResult = super.calculate(key);
            lruCache.put(key, currentResult);
            return currentResult;
        }
    }
}
