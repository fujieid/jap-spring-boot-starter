package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.core.store.SessionJapUserStore;
import com.fujieid.jap.spring.boot.starter.support.cache.RedisAuthStateCache;
import com.fujieid.jap.spring.boot.starter.support.cache.RedisJapCache;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
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
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public JapCache japCache(JapProperties japProperties,
                             BeanFactory beanFactory,
                             RedisTemplate<String, Serializable> redisTemplate){
        JapCache japCache = new JapLocalCache();
        CacheProperties.CacheType type = japProperties.getCache().getType();

        if (type.equals(CacheProperties.CacheType.REDIS)){

            japCache = new RedisJapCache(redisTemplate,japProperties.getCache());
        } else if (type.equals(CacheProperties.CacheType.CUSTOM)) {
            // TODO: 2021/8/9 实现自定义cache
//            ((DefaultListableBeanFactory)beanFactory).registerSingleton("customJapCache",);
        }
        return japCache;
    }

    // TODO: 2021/8/13 实现，考虑SessionJapUserStore还是SsoJapUserStore。这个接口比较特殊，似乎并不欢迎用自己实现的方式。
    //  可以看到public AbstractJapStrategy(JapUserService japUserService, JapConfig japConfig, JapUserStore japUserStore, JapCache japCache)
    //  这个构造器没有被四种strategy的任何一个直接调用
    @Bean
    public JapUserStore japUserStore(JapProperties japProperties) {
        return new SessionJapUserStore();
    }


    // TODO: 2021/8/13 有两种策略，下面这种，或采用justauth-spring-boot-starter中的实现和配置
    @Bean
    public AuthStateCache authStateCache(JapProperties japProperties, RedisTemplate<String,String> redisTemplate){
        AuthStateCache authStateCache = AuthDefaultStateCache.INSTANCE;
        CacheProperties.CacheType cacheType = japProperties.getCache().getType();
        if (cacheType.equals(CacheProperties.CacheType.REDIS)){
            authStateCache = new RedisAuthStateCache(redisTemplate, japProperties.getCache());
        }

        return authStateCache;
    }

}
