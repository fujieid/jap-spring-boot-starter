package com.fujieid.jap.spring.boot.starter.support.cache;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.spring.boot.starter.autoconfigure.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class RedisJapCache implements JapCache {
    private final RedisTemplate<String,Serializable> redisTemplate;
    private final CacheProperties cacheProperties;

    public RedisJapCache(RedisTemplate<String,Serializable> redisTemplate, CacheProperties cacheProperties){
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }

    @Override
    public void set(String key, Serializable value) {
        redisTemplate.opsForValue().set(cacheProperties.getPrefix()+key,value,cacheProperties.getTimeout());
    }

    /**
     * 指定缓存时间，单位：毫秒
     * @param key
     * @param value
     * @param timeout
     */
    @Override
    public void set(String key, Serializable value, long timeout) {
        redisTemplate.opsForValue().set(cacheProperties.getPrefix()+key, value,timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Serializable get(String key) {
        return redisTemplate.opsForValue().get(cacheProperties.getPrefix()+key);
    }

    @Override
    public boolean containsKey(String key) {
        Long expire = redisTemplate.getExpire(cacheProperties.getPrefix() + key, TimeUnit.MILLISECONDS);//单位 毫秒
        if (ObjectUtil.isNull(expire))
            expire=0L;
        return expire>0;
    }

    @Override
    public void removeKey(String key) {
        redisTemplate.delete(key);
    }
}
