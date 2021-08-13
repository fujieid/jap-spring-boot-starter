package com.fujieid.jap.spring.boot.starter.support.cache;

import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.spring.boot.starter.autoconfigure.CacheProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: 2021/8/13
public class RedisJapUserStore implements JapUserStore {
    private RedisTemplate<?,?> redisTemplate;
    private CacheProperties cacheProperties;

    public RedisJapUserStore(RedisTemplate<?, ?> redisTemplate, CacheProperties cacheProperties) {
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }
    @Override
    public JapUser save(HttpServletRequest request, HttpServletResponse response, JapUser japUser) {
        return null;
    }

    @Override
    public void remove(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public JapUser get(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
