package com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapBasicProperties;
import com.fujieid.jap.spring.boot.common.autoconfigure.CacheType;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.spring.boot.japsocialspringbootstarter.cache.RedisAuthStateCache;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties({SocialProperties.class, SocialCacheProperties.class})
@Slf4j
public class SocialAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext,
                                         JapBasicProperties basicProperties,
                                         SocialProperties socialProperties,
                                         AuthStateCache authStateCache,
                                         JapCache japCache){
        JapUserService userService = JapUtil.getUserService(applicationContext, JapUserServiceType.SOCIAL, socialProperties.getSocialUserService());
        return new SocialStrategy(userService, basicProperties.getBasic(), japCache, authStateCache);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthStateCache authStateCache(SocialCacheProperties socialCacheProperties,
                                         RedisTemplate<String, String> redisTemplate){
        CacheType type = socialCacheProperties.getType();
        if (type.equals(CacheType.DEFAULT))
            return AuthDefaultStateCache.INSTANCE;
        if (type.equals(CacheType.REDIS)){
            return new RedisAuthStateCache(redisTemplate,socialCacheProperties);
        }
        log.warn("没有指定SocialStrategy的缓存，请自定义");
        return null;

    }
}
