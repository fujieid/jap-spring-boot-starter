package com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapOauth2Exception;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapBasicProperties;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.spring.boot.japoauth2springbootstarter.Oauth2Operations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({Oauth2Properties.class})
@Slf4j
public class Oauth2AutoConfiguration {
    final private String NO_USE_THIS_STRATEGY="没有使用oauth策略";
    final private String MISS_CONFIG_INFO="缺少Oauth相关配置，请在application.properties/yml文件中进行配置";
    @Bean
    @ConditionalOnMissingBean
    public Oauth2Strategy oauth2Strategy(ApplicationContext applicationContext,
                                         JapBasicProperties basicProperties,
                                         Oauth2Properties oauth2Properties,
                                         JapCache japCache) {
        try {
            JapUserService userService = JapUtil.getUserService(applicationContext, JapUserServiceType.OAUTH2, oauth2Properties.getOauth2UserService());
            return new Oauth2Strategy(userService, basicProperties.getBasic(), japCache);
        } catch (BeansException | IllegalArgumentException e){
            log.warn(NO_USE_THIS_STRATEGY);
            return null;
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public Oauth2Operations oauth2Operations(Oauth2Strategy oauth2Strategy, Oauth2Properties oauth2Properties){
        if(ObjectUtil.isNull(oauth2Strategy))
            return null;
        if (oauth2Properties.getOauth2().isEmpty())
            throw new JapOauth2Exception(MISS_CONFIG_INFO);
        return new Oauth2Operations(oauth2Strategy, oauth2Properties);
    }
}