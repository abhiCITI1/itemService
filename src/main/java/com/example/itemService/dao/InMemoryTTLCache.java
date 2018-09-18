package com.example.itemService.dao;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTTLCache<String,ItemInfo> extends ConcurrentHashMap<String,ItemInfo> {

    private static final long serialVersionUID = 1L;

    private Map<String, Long> timeMap = new ConcurrentHashMap<String, Long>();
    private long expiryInMillis = 2000;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss:SSS");

    public InMemoryTTLCache() {
        initialize();
    }

    public InMemoryTTLCache(long expiryInMillis) {
        this.expiryInMillis = expiryInMillis;
        initialize();
    }

    void initialize() {
        new CleanerThread().start();
    }


    @Override
    public ItemInfo put(String key, ItemInfo value) {
        Date date = new Date();
        timeMap.put(key, date.getTime());
        System.out.println("Inserting : " + sdf.format(date) + " : " + key + " : " + value);
        super.put(key, value);
        return value;
    }

    @Override
    public void putAll(Map<? extends String, ? extends ItemInfo> m) {
        for (String key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public ItemInfo putIfAbsent(String key, ItemInfo value) {
        if (!containsKey(key))
            return put(key, value);
        else
            return get(key);
    }


    class CleanerThread extends Thread {
        @Override
        public void run() {
            System.out.println("Initiating Cleaner Thread..");
            while (true) {
                cleanMap();
                try {
                    Thread.sleep(expiryInMillis / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cleanMap() {
            long currentTime = new Date().getTime();
            for (String key : timeMap.keySet()) {
                if (currentTime > (timeMap.get(key) + expiryInMillis)) {
                    ItemInfo value = remove(key);
                    timeMap.remove(key);
                    System.out.println("Removing : " + sdf.format(new Date()) + " : " + key + " : " + value);
                }
            }
        }
    }


}
