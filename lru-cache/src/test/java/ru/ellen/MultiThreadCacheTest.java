package ru.ellen;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadCacheTest {

    private LruCache lruCache;

    @Test
    public void multipleThreadTest() {

        // Запускаем 1000 раз, увеличивая случайность
        for (int i = 0; i < 999; i++) {
            lruCache = new MultiThreadCache(3);
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

            // check size
            Assert.assertEquals(3, lruCache.getCache().size());

            // check last elements
            Assert.assertFalse(lruCache.getCache().contains("7t3eecb"));
            Assert.assertTrue(lruCache.getCache().contains("1"));
            Assert.assertTrue(lruCache.getCache().contains("22"));
            Assert.assertTrue(lruCache.getCache().contains("55555"));
        }
    }

    @Test
    public void cacheGetSingleThreadTest() {
        lruCache = new MultiThreadCache(3);

        lruCache.put("1", 1);
        lruCache.put("2", 2);
        lruCache.put("3", 3);
        lruCache.put("4", 4);
        lruCache.put("5", 5);
        lruCache.put("4", 7);

        Assert.assertEquals(Optional.empty(), lruCache.get("2"));
        Assert.assertEquals(7, lruCache.get("4").get());
    }

    @Test
    public void cacheGetMultipleThreadTest() {
        lruCache = new MultiThreadCache(3);

        lruCache.put("1", 1);
        lruCache.put("2", 2);
        lruCache.put("3", 3);
        lruCache.put("4", 4);
        lruCache.put("5", 5);
        lruCache.put("4", 7);

        ExecutorService service = Executors.newFixedThreadPool(15);
        for (int j = 0; j < 15; j++) {
            service.execute(new CacheGetter());
        }
        service.shutdown();
    }

    private class CacheWorker implements Runnable {

        @Override
        public void run() {
            ArrayList<String> data = generateData();
            for (String elem : data) {
                lruCache.put(elem, elem.length() * 2 + 19);
            }
        }
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

    private class CacheGetter implements Runnable {

        private final String[] keys = new String[]{"3", "4", "5"};
        private final ArrayList<Integer> values = new ArrayList<>(Arrays.asList(3, 7, 5));

        @Override
        public void run() {
            // get element five times
            for (int i = 0; i < 5; i++) {
                Random r = new Random();
                String key = keys[r.nextInt(3)];
                Assert.assertTrue(values.contains(lruCache.get(key).get()));
            }
        }
    }
}
