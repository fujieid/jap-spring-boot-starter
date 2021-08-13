package com.fujieid.jap.spring.boot.starter.support.cache;

import com.fujieid.jap.spring.boot.starter.autoconfigure.CacheProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.data.redis.core.RedisTemplate;

// TODO: 2021/8/13
public class RedisAuthStateCache implements AuthStateCache {
    private RedisTemplate<String, String> redisTemplate;
    private CacheProperties cacheProperties;

    public RedisAuthStateCache(RedisTemplate<String, String> redisTemplate,
                               CacheProperties cacheProperties){
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }

    @Override

    public void cache(String key, String value) {

    }

    @Override
    public void cache(String key, String value, long timeout) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }
}
