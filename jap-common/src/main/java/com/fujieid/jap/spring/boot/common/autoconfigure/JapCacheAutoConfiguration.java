package com.fujieid.jap.spring.boot.common.autoconfigure;

import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.core.exception.JapSocialException;
import com.fujieid.jap.spring.boot.common.cache.RedisJapCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

@Slf4j
public class JapCacheAutoConfiguration {

    /**
     * redis jap token cache
     */
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnProperty(name = "jap.cache.token.type", havingValue = "redis")
    @AutoConfigureAfter(JapBasicAutoConfiguration.class)
    static class Redis{

        @Bean
        public RedisTemplate<String, Serializable> serializableRedisTemplate(RedisConnectionFactory connectionFactory){
            RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(connectionFactory);
            return redisTemplate;
        }

        @Bean
        @ConditionalOnMissingBean
        public JapCache japCache(CacheProperties cacheProperties,
                                 RedisTemplate<String, Serializable> redisTemplate){
            log.info("将redis作为jap cache");
            return new RedisJapCache(redisTemplate, cacheProperties);
        }
    }

    /**
     * default jap token cache
     */
    @ConditionalOnProperty(name = "jap.cache.token.type", havingValue = "default", matchIfMissing = true)
    @AutoConfigureAfter(JapBasicAutoConfiguration.class)
    static class Default{
        @Bean
        @ConditionalOnMissingBean
        public JapCache japCache(){
            log.info("默认jap cache");
            return new JapLocalCache();
        }
    }

    /**
     * custom jap token cache
     */
    @ConditionalOnProperty(name = "jap.cache.token.type", havingValue = "custom")
    @AutoConfigureAfter(JapBasicAutoConfiguration.class)
    static class Custom{
        @Bean
        @ConditionalOnMissingBean
        public JapCache japCache(){
            // 由于auto-configuration是晚于Externalized Configuration的，所以如果此时没有JapCache bean，说明用户并没有完成自定义，因此可以直接抛出错误
            log.error("缺少com.fujieid.jap.core.cache.JapCache实例，请自行实现");
            throw new JapSocialException("缺少com.fujieid.jap.core.cache.JapCache实例，请自行实现");
        }
    }

}
