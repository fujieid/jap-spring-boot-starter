package com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure;

import cn.hutool.core.text.StrFormatter;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.OidcException;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapBasicAutoConfiguration;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapBasicProperties;
import com.fujieid.jap.spring.boot.common.JapUserServiceType;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({OidcProperties.class})
@AutoConfigureAfter({JapBasicAutoConfiguration.class})
@Slf4j
public class OidcAutoConfiguration {
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
            String error = StrFormatter.format(JapUtil.STRATEGY_NO_USERSERVICE,"OidcStrategy");
            log.error(error);
            throw new OidcException(error, e.getCause());
        }

    }
}
