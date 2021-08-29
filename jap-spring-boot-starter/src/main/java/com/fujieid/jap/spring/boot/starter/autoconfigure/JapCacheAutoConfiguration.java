package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.core.store.SessionJapUserStore;
import com.fujieid.jap.spring.boot.starter.support.cache.RedisAuthStateCache;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * 缓存配置
 */
@Configuration
@EnableConfigurationProperties(value = {JapProperties.class})
public class JapCacheAutoConfiguration {

    @Bean
    public RedisTemplate<String,Serializable> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }



    // TODO: 2021/8/13 有两种策略，下面这种，或采用justauth-spring-boot-starter中的实现和配置
    @Bean
    public AuthStateCache authStateCache(JapProperties japProperties){
        AuthStateCache authStateCache = AuthDefaultStateCache.INSTANCE;
        authStateCache = new RedisAuthStateCache(null, japProperties.getCache());

        return authStateCache;
    }

    // TODO: 2021/8/13 实现，考虑SessionJapUserStore还是SsoJapUserStore
    @Bean
    public JapUserStore japUserStore(JapProperties japProperties) {
        return new SessionJapUserStore();
    }

}