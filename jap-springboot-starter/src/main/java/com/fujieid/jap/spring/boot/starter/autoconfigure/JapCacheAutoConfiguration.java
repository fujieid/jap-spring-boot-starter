package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.spring.boot.starter.support.cache.RedisJapCache;
import org.springframework.beans.factory.BeanFactory;
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

    @Bean
    public JapCache japCache(JapProperties japProperties,
                             BeanFactory beanFactory,
                             RedisTemplate<String, Serializable> redisTemplate){
        JapCache japCache = new JapLocalCache();
        CacheProperties.CacheType type = japProperties.getCache().getType();
        if (type.equals(CacheProperties.CacheType.REDIS)) japCache = new RedisJapCache(redisTemplate,japProperties.getCache());
        if (type.equals(CacheProperties.CacheType.CUSTOM)) {
            // TODO: 2021/8/9 实现自定义cache
//            ((DefaultListableBeanFactory)beanFactory).registerSingleton("customJapCache",);
        }
        return japCache;
    }

}
