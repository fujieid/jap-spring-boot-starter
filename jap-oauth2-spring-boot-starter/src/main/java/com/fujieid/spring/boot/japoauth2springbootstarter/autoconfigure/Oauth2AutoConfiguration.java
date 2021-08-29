package com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.starter.support.util.JapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class Oauth2AutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Oauth2Strategy oauth2Strategy(ApplicationContext applicationContext,
                                         JapProperties japProperties,
                                         JapCache japCache) {
        try{
            JapUserService oauth2 = JapUtil.getUserService(applicationContext, JapUserServiceType.OAUTH2,japProperties.getOauth2UserService());
            return new Oauth2Strategy(oauth2, japProperties.getBasic(), japCache);
        } catch (Exception e){
            log.warn("尚未指定oauth2Strategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new Oauth2Strategy(null, japProperties.getBasic(), japCache);
        }
    }
}
