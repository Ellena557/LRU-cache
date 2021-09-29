package ru.ellen.example;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class CustomAlgorithmCounterTest {

    @Test
    public void comparisonTest() {
        AlgorithmCounter algorithmCounter = new CachedAlgorithmCounter(3);
        CustomAlgorithmCounter customAlgorithmCounter = new CustomAlgorithmCounter(3);

        ArrayList<String> data = generateData();
        for (String elem : data) {
            algorithmCounter.algorithm(elem);
            customAlgorithmCounter.algorithm(elem);
        }

        Assert.assertTrue(customAlgorithmCounter.getCache().containsAll(
                algorithmCounter.getCache()
        ));
    }

    private ArrayList<String> generateData() {
        ArrayList<String> res = new ArrayList<>();
        res.add("1");
        res.add("22");
        res.add("333");
        res.add("333");
        res.add("4444");
        res.add("55555");
        res.add("333");
        res.add("4444");
        res.add("7t3eecb");
        res.add("8397");
        res.add("UG3EOYQUWG");
        res.add("849714");
        res.add("hjcaGUE");
        res.add("4444");
        res.add("55555");
        res.add("333");
        res.add("4444");
        res.add("55555");
        res.add("1");
        res.add("22");
        res.add("22");
        res.add("55555");

        return res;
    }
}
