package ru.ellen;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
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

            Assert.assertEquals(3, lruCache.getCache().size());
        }
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
}