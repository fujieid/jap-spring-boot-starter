package com.fujieid.jap.spring.boot.starter.support.cache;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.spring.boot.starter.autoconfigure.CacheProperties;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

// TODO: 2021/8/13

/**
 * 这是用于social的存储
 */
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
        redisTemplate.opsForValue().set(cacheProperties.getAuthStatePrefix()+key, value,cacheProperties.getTimeout());
    }

    @Override
    public void cache(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(cacheProperties.getAuthStatePrefix()+key, value, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(cacheProperties.getAuthStatePrefix()+key);
    }

    @Override
    public boolean containsKey(String key) {
        Long expire = redisTemplate.getExpire(cacheProperties.getAuthStatePrefix() + key, TimeUnit.MILLISECONDS);//单位 毫秒
        if (ObjectUtil.isNull(expire))
            expire=0L;
        return expire>0;
    }
}
