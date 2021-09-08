package com.fujieid.jap.spring.boot.common.cache;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.spring.boot.common.autoconfigure.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

// TODO: 2021/8/13 是否需要考虑线程安全？是否需要加锁?

/**
 * 这是存token的接口实现
 */
public class RedisJapCache implements JapCache {
    private final RedisTemplate<String,Serializable> redisTemplate;
    private final CacheProperties cacheProperties;

    public RedisJapCache(RedisTemplate<String,Serializable> redisTemplate, CacheProperties cacheProperties){
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }

    @Override
    public void set(String key, Serializable value) {
        redisTemplate.opsForValue().set(cacheProperties.getCachePrefix()+key,value,cacheProperties.getTimeout());
    }

    /**
     * 指定缓存时间，单位：毫秒
     * @param key
     * @param value
     * @param timeout
     */
    @Override
    public void set(String key, Serializable value, long timeout) {
        redisTemplate.opsForValue().set(cacheProperties.getCachePrefix()+key, value,timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Serializable get(String key) {
        return redisTemplate.opsForValue().get(cacheProperties.getCachePrefix()+key);
    }

    @Override
    public boolean containsKey(String key) {
        Long expire = redisTemplate.getExpire(cacheProperties.getCachePrefix() + key, TimeUnit.MILLISECONDS);//单位 毫秒
        if (ObjectUtil.isNull(expire))
            expire=0L;
        return expire>0;
    }

    @Override
    public void removeKey(String key) {
        redisTemplate.delete(cacheProperties.getCachePrefix()+key);
    }
}
