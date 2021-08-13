package com.fujieid.jap.spring.boot.starter;

import com.fujieid.jap.core.cache.JapCache;

import java.io.Serializable;

public class MyJapCache implements JapCache {
    @Override
    public void set(String key, Serializable value) {

    }

    @Override
    public void set(String key, Serializable value, long timeout) {

    }

    @Override
    public Serializable get(String key) {
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public void removeKey(String key) {

    }
}
