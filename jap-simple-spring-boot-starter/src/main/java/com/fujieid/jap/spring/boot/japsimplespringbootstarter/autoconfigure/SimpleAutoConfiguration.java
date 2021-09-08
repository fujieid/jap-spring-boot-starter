package com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.simple.SimpleStrategy;
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
@EnableConfigurationProperties({SimpleProperties.class})
@Slf4j
public class SimpleAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext,
                                         SimpleProperties simpleProperties,
                                         JapBasicProperties basicProperties,
                                         JapCache japCache){
        JapUserService userService = JapUtil.getUserService(applicationContext, JapUserServiceType.SIMPLE, simpleProperties.getSimpleUserService());
        return new SimpleStrategy(userService, basicProperties.getBasic(), japCache);
    }
}
