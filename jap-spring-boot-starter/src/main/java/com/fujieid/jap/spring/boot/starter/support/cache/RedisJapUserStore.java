package com.fujieid.jap.spring.boot.starter.support.cache;

import com.fujieid.jap.core.JapConst;
import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.spring.boot.starter.autoconfigure.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// TODO: 2021/8/13

/**
 * 这是用于crud用户登录信息的接口实现，根据cookie中的
 * JSESSIONID=936964A4658342CBC8BF99723DDA8B99
 */
public class RedisJapUserStore implements JapUserStore {
    private RedisTemplate<String,JapUser> redisTemplate;
    private CacheProperties cacheProperties;

    public RedisJapUserStore(RedisTemplate<String,JapUser> redisTemplate, CacheProperties cacheProperties) {
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }
    @Override
    public JapUser save(HttpServletRequest request, HttpServletResponse response, JapUser japUser) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("JSESSIONID")){
                redisTemplate.opsForValue().set(cacheProperties.getUserStorePrefix()+cookie.getValue(),japUser);
                return japUser;
            }
        }
        return null;
    }

    @Override
    public void remove(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public JapUser get(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("JSESSIONID")){
                return (JapUser) redisTemplate.opsForValue().get(cacheProperties.getUserStorePrefix()+cookie.getValue());
            }
        }
        return null;
    }
}
