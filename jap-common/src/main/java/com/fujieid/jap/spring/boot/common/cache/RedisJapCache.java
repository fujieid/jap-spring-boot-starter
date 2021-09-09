package com.fujieid.jap.spring.boot.common.cache;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.spring.boot.common.autoconfigure.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

// TODO: 2021/8/13 是否需要考虑线程安全？是否需要加锁?

/**
 * token缓存的redis实现
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
        redisTemplate.opsForValue().set(cacheProperties.getToken().getPrefix()+key,value,cacheProperties.getToken().getExpireTime());
    }

    /**
     * Set the value and expiration timeout for key.
     * @param key must not be null
     * @param value must not be null
     * @param timeout unit:MILLISECONDS
     */
    @Override
    public void set(String key, Serializable value, long timeout) {
        redisTemplate.opsForValue().set(cacheProperties.getToken().getPrefix()+key, value,timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Serializable get(String key) {
        return redisTemplate.opsForValue().get(cacheProperties.getToken().getPrefix()+key);
    }

    @Override
    public boolean containsKey(String key) {
        Long expire = redisTemplate.getExpire(cacheProperties.getToken().getPrefix() + key, TimeUnit.MILLISECONDS);//单位 毫秒
        if (ObjectUtil.isNull(expire))
            return false;
        return expire>0;
    }

    @Override
    public void removeKey(String key) {
        redisTemplate.delete(cacheProperties.getToken().getPrefix()+key);
    }
}
