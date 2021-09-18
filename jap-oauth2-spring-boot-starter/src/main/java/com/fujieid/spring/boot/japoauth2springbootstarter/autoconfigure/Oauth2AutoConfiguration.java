package com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure;

import cn.hutool.core.text.StrFormatter;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapOauth2Exception;
import com.fujieid.jap.oauth2.Oauth2Strategy;
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

@EnableConfigurationProperties({Oauth2Properties.class})
@AutoConfigureAfter({JapBasicAutoConfiguration.class})
@Slf4j
public class Oauth2AutoConfiguration {
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
            String error = StrFormatter.format(JapUtil.STRATEGY_NO_USERSERVICE,"Oauth2Strategy");
            log.error(error);
            throw new JapOauth2Exception(error, e.getCause());
        }
    }
}