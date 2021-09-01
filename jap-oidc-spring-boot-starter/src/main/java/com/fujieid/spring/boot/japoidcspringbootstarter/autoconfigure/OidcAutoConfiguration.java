package com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
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
public class OidcAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public OidcStrategy oidcStrategy(ApplicationContext applicationContext,
                                     JapProperties japProperties,
                                     JapCache japCache,
                                     JapTemplate japTemplate) {
        try{
            JapUserService oidc = JapUtil.getUserService(applicationContext, JapUserServiceType.OIDC,japProperties.getOidcUserService());
            OidcStrategy oidcStrategy = new OidcStrategy(oidc,japProperties.getBasic(), japCache);
            japTemplate.setOidcStrategy(oidcStrategy);
            return oidcStrategy;
        } catch (Exception e){
            throw new JapException("尚未指定oidcStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
        }

    }
}
