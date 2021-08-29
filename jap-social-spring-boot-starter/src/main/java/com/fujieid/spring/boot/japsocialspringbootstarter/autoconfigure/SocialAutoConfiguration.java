package com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.starter.support.util.JapUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SocialAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext,
                                         JapProperties japProperties,
                                         JapCache japCache,
                                         AuthStateCache authStateCache) {
        try {
            JapUserService social = JapUtil.getUserService(applicationContext, JapUserServiceType.SOCIAL, japProperties.getSocialUserService());

            return new SocialStrategy(social, japProperties.getBasic(), japCache,authStateCache);
        } catch (Exception e){
            log.warn("尚未指定socialStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new SocialStrategy(null,japProperties.getBasic(),japCache);
        }
    }
}
