package ru.ellen.example;

import java.util.Set;

public abstract class AlgorithmCounter {

    public abstract Set<String> getCache();

    public Object calculate(String key) {
        return key.length() * 5 + 1;
    }
}
