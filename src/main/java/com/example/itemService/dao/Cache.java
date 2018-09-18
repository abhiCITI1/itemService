package com.example.itemService.dao;

public interface Cache {

    void add(String key, Object value, long timeInMillis);

    void remove(String key);

    void clear();

    Object get(String key);

    long size();


}
