package com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapBasicProperties;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.SimpleOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({SimpleProperties.class})
@Slf4j
public class SimpleAutoConfiguration {
    final private String NO_USE_THIS_STRATEGY="没有使用oauth策略";
    final private String MISS_CONFIG_INFO="缺少Oauth相关配置，请在application.properties/yml文件中进行配置";
    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext,
                                         SimpleProperties simpleProperties,
                                         JapBasicProperties basicProperties,
                                         JapCache japCache){
        try {
            JapUserService userService = JapUtil.getUserService(applicationContext, JapUserServiceType.SIMPLE, simpleProperties.getSimpleUserService());
            return new SimpleStrategy(userService, basicProperties.getBasic(), japCache);
        } catch (BeansException | IllegalArgumentException e){
            log.warn(NO_USE_THIS_STRATEGY);
            return null;
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleOperations simpleOperations(SimpleStrategy simpleStrategy, SimpleProperties simpleProperties){
        if (ObjectUtil.isNull(simpleStrategy))
            return null;
        if (ObjectUtil.isNull(simpleProperties.getSimple()))
            throw new JapException(MISS_CONFIG_INFO);
        return new SimpleOperations(simpleStrategy, simpleProperties);
    }
}
