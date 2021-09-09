package com.fujieid.spring.boot.japsocialspringbootstarter.cache;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure.SocialCacheProperties;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

// TODO: 2021/8/13

/**
 * 用于SocialStrategy的缓存
 */
public class RedisAuthStateCache implements AuthStateCache {
    private RedisTemplate<String, String> redisTemplate;
    private SocialCacheProperties socialCacheProperties;

    public RedisAuthStateCache(RedisTemplate<String, String> redisTemplate,
                               SocialCacheProperties socialCacheProperties){
        this.redisTemplate = redisTemplate;
        this.socialCacheProperties = socialCacheProperties;
    }

    @Override
    public void cache(String key, String value) {
        redisTemplate.opsForValue().set(socialCacheProperties.getPrefix()+key, value, socialCacheProperties.getTimeout());
    }

    @Override
    public void cache(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(socialCacheProperties.getPrefix()+key, value, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(socialCacheProperties.getPrefix()+key);
    }

    @Override
    public boolean containsKey(String key) {
        Long expire = redisTemplate.getExpire(socialCacheProperties.getPrefix() + key, TimeUnit.MILLISECONDS);//单位 毫秒
        if (ObjectUtil.isNull(expire))
            return false;
        return expire>0;
    }
}
