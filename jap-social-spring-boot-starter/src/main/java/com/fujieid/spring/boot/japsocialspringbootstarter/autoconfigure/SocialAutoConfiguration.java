package com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapOauth2Exception;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapBasicProperties;
import com.fujieid.jap.spring.boot.common.autoconfigure.CacheType;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.spring.boot.japsocialspringbootstarter.SocialOperations;
import com.fujieid.spring.boot.japsocialspringbootstarter.cache.RedisAuthStateCache;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.beans.BeansException;
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
    final private String NO_USE_THIS_STRATEGY="没有使用oauth策略";
    final private String MISS_CONFIG_INFO="缺少Oauth相关配置，请在application.properties/yml文件中进行配置";
    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext,
                                         JapBasicProperties basicProperties,
                                         SocialProperties socialProperties,
                                         AuthStateCache authStateCache,
                                         JapCache japCache){
        try {
            JapUserService userService = JapUtil.getUserService(applicationContext, JapUserServiceType.SOCIAL, socialProperties.getSocialUserService());
            return new SocialStrategy(userService, basicProperties.getBasic(), japCache, authStateCache);
        } catch (BeansException | IllegalArgumentException e){
            log.warn(NO_USE_THIS_STRATEGY);
            return null;
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public SocialOperations socialOperations(SocialStrategy socialStrategy, SocialProperties socialProperties){
        if(ObjectUtil.isNull(socialStrategy))
            return null;
        if (socialProperties.getSocial().isEmpty())
            throw new JapOauth2Exception("缺少SocialConfig配置");
        return new SocialOperations(socialStrategy, socialProperties);
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
        log.warn(MISS_CONFIG_INFO);
        return null;

    }
}
