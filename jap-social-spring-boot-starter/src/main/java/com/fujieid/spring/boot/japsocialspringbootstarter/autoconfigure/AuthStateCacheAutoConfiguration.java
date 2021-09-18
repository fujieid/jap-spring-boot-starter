package com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure;

import com.fujieid.jap.core.exception.JapSocialException;
import com.fujieid.spring.boot.japsocialspringbootstarter.cache.RedisAuthStateCache;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class AuthStateCacheAutoConfiguration {

    /**
     * redis
     */
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnProperty(name = "jap.social.cache.type",havingValue = "redis")
    @AutoConfigureBefore(SocialAutoConfiguration.class)
    static class Redis{
        @Bean
        @ConditionalOnMissingBean
        public AuthStateCache authStateCache(SocialCacheProperties socialCacheProperties,
                                             RedisTemplate<String, String> redisTemplate){
            log.info("使用 redis 缓存social");
            return new RedisAuthStateCache(redisTemplate,socialCacheProperties);
        }
    }

    /**
     * 默认
     */
    @ConditionalOnProperty(name = "jap.social.cache.type",havingValue = "default", matchIfMissing = true)
    @AutoConfigureBefore(SocialAutoConfiguration.class)
    static class Default{
        @Bean
        @ConditionalOnMissingBean
        public AuthStateCache authStateCache(){
            log.info("使用social的 默认 缓存");
            return AuthDefaultStateCache.INSTANCE;
        }
    }

    /**
     * custom
     */
    @ConditionalOnProperty(name = "jap.social.cache.type",havingValue = "custom")
    @AutoConfigureBefore(SocialAutoConfiguration.class)
    static class Custom{
        @Bean
        @ConditionalOnMissingBean
        public AuthStateCache authStateCache(){
            // 由于auto-configuration是晚于Externalized Configuration的，所以如果此时没有AuthStateCache bean，说明用户并没有完成自定义，因此可以直接抛出错误
            log.error("缺少me.zhyd.oauth.cache.AuthStateCache实例，请自行实现");
            throw new JapSocialException("缺少me.zhyd.oauth.cache.AuthStateCache实例，请自行实现");
        }
    }
}
