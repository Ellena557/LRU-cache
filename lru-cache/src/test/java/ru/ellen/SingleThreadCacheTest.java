package ru.ellen;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadCacheTest {

    private LruCache lruCache;

    @Test
    public void singleThreadTest() {
        lruCache = new SingleThreadCache(3);
        ArrayList<String> data = generateData();
        for (String elem : data) {
            lruCache.put(elem, elem.length());
        }

        Set cache = lruCache.getCache();

        Assert.assertTrue(cache.contains("1"));
        Assert.assertTrue(cache.contains("22"));
        Assert.assertTrue(cache.contains("55555"));

        Assert.assertEquals(3, cache.size());
    }

    @Test
    public void multipleThreadTest() {

        ArrayList<Integer> sizes = new ArrayList<>();

        // Запускаем 100 раз, увеличивая случайность
        for (int i = 0; i < 99; i++) {
            lruCache = new SingleThreadCache(3);
            ExecutorService service = Executors.newFixedThreadPool(10);
            for (int j = 0; j < 10; j++) {
                service.execute(new CachePutter());
            }
            service.shutdown();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sizes.add(lruCache.getCache().size());
        }

        boolean cacheIsOk = sizes.stream().allMatch(el -> el == 3);
        boolean containsGreater = sizes.stream().anyMatch(el -> el > 3);

        Assert.assertFalse(cacheIsOk);
        Assert.assertTrue(containsGreater);
    }

    @Test
    public void cacheWithGetTest() {
        lruCache = new SingleThreadCache(3);

        lruCache.put("1", 1);
        lruCache.put("2", 2);
        lruCache.put("3", 3);
        lruCache.put("4", 4);
        lruCache.put("5", 5);
        lruCache.put("4", 7);

        Assert.assertEquals(Optional.empty(), lruCache.get("2"));
        Assert.assertEquals(7, lruCache.get("4").get());
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

    private class CachePutter implements Runnable {

        @Override
        public void run() {
            ArrayList<String> data = generateData();
            for (String elem : data) {
                lruCache.put(elem, elem.length() + 7);
            }
        }
    }
}
