package ru.ellen.example;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CachedAlgorithmCounterTest {
private AlgorithmCounter algorithmCounter;

    @Test
    public void singleThreadAlgorithmTest() {
        algorithmCounter = new CachedAlgorithmCounter(3);
        ArrayList<String> data = generateData();
        for (String elem : data) {
            algorithmCounter.algorithm(elem);
        }

        Set cache = algorithmCounter.getCache();

        Assert.assertTrue(cache.contains("1"));
        Assert.assertTrue(cache.contains("22"));
        Assert.assertTrue(cache.contains("55555"));

        Assert.assertEquals(3, algorithmCounter.getCache().size());
    }

    @Test
    public void multipleThreadAlgorithmTest() {

        ArrayList<Integer> sizes = new ArrayList<>();

        // Запускаем 10 раз, увеличивая случайность
        for (int i = 0; i < 99; i++) {
            algorithmCounter = new CachedAlgorithmCounter(3);
            ExecutorService service = Executors.newFixedThreadPool(10);
            for (int j = 0; j < 10; j++) {
                service.execute(new CacheWorker());
            }
            service.shutdown();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sizes.add(algorithmCounter.getCache().size());
        }

        boolean cacheIsOk = sizes.stream().allMatch(el -> el == 3);
        boolean containsGreater = sizes.stream().anyMatch(el -> el > 3);

        Assert.assertFalse(cacheIsOk);
        Assert.assertTrue(containsGreater);
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

    private class CacheWorker implements Runnable {

        @Override
        public void run() {
            ArrayList<String> data = generateData();
            for (String elem : data) {
                algorithmCounter.algorithm(elem);
            }
        }
    }
}
