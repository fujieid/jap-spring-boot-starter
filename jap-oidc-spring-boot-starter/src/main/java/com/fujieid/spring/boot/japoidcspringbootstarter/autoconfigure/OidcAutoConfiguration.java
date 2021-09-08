package com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapBasicProperties;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OidcProperties.class})
@Slf4j
public class OidcAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public OidcStrategy oidcStrategy(ApplicationContext applicationContext,
                                     JapBasicProperties basicProperties,
                                     OidcProperties oidcProperties,
                                     JapCache japCache) {
        JapUserService userService = JapUtil.getUserService(applicationContext, JapUserServiceType.OIDC, oidcProperties.getOidcUserService());
        return new OidcStrategy(userService, basicProperties.getBasic(), japCache);
    }
}
