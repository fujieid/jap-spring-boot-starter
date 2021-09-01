package com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.simple.SimpleStrategy;
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
public class SimpleAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext,
                                         JapTemplate japTemplate,
                                         JapProperties japProperties,
                                         JapCache japCache) {
        try {
            JapUserService simple = JapUtil.getUserService(applicationContext, JapUserServiceType.SIMPLE,japProperties.getSimpleUserService());
            SimpleStrategy simpleStrategy = new SimpleStrategy(simple, japProperties.getBasic(), japCache);
            japTemplate.setSimpleStrategy(simpleStrategy);
            return simpleStrategy;
        } catch (Exception e){
            throw new JapException("尚未指定simpleStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
        }

    }
}
