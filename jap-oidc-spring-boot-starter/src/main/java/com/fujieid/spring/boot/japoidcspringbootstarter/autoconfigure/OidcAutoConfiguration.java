package com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.exception.OidcException;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapBasicProperties;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.spring.boot.japoidcspringbootstarter.OidcOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OidcProperties.class})
@Slf4j
public class OidcAutoConfiguration {
    final private String NO_USE_THIS_STRATEGY="没有使用oauth策略";
    final private String MISS_CONFIG_INFO="缺少Oauth相关配置，请在application.properties/yml文件中进行配置";
    @Bean
    @ConditionalOnMissingBean
    public OidcStrategy oidcStrategy(ApplicationContext applicationContext,
                                     JapBasicProperties basicProperties,
                                     OidcProperties oidcProperties,
                                     JapCache japCache) {
        try {
            JapUserService userService = JapUtil.getUserService(applicationContext, JapUserServiceType.OIDC, oidcProperties.getOidcUserService());
            return new OidcStrategy(userService, basicProperties.getBasic(), japCache);
        } catch (BeansException | IllegalArgumentException e){
            log.warn(NO_USE_THIS_STRATEGY);
            return null;
        }

    }

    @Bean
    @ConditionalOnMissingBean
    public OidcOperations oidcOperations(OidcStrategy oidcStrategy, OidcProperties oidcProperties){
        if (ObjectUtil.isNull(oidcStrategy))
            return null;
        if (oidcProperties.getOidc().isEmpty())
            throw new OidcException(MISS_CONFIG_INFO);
        return new OidcOperations(oidcStrategy,oidcProperties);
    }
}
