package com.fujieid.jap.spring.boot.starter.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.JapStrategyFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {JapProperties.class, AuthProperties.class})
public class JapAutoConfiguration {
    @Bean
//    @ConditionalOnBean(value = {SimpleStrategy.class, SocialStrategy.class})//当所有的strategy都注入进了bean容器中才创建这个bean
    @ConditionalOnMissingBean
    public JapStrategyFactory japStrategyFactory(JapProperties japProperties, AuthProperties authProperties){
        japProperties.getSocial().setJustAuthConfig(authProperties);
        Strategy.SIMPLE.setConfig(japProperties.getSimple());
        Strategy.SOCIAL.setConfig(japProperties.getSocial());

        return new JapStrategyFactory(japProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext, JapProperties japProperties) {
        JapUserService simple = applicationContext.containsBean("simple") ?
                (JapUserService) applicationContext.getBean("simple") :
                (JapUserService) applicationContext.getBean(japProperties.getSimpleUserService());

        if(ObjectUtil.isNull(simple))
            throw new NoSuchBeanDefinitionException("SimpleStrategy 需要的japUserService未指定");

        return new SimpleStrategy(simple, japProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext, JapProperties japProperties) {
        JapUserService social = applicationContext.containsBean("social") ?
                (JapUserService) applicationContext.getBean("social") :
                (JapUserService) applicationContext.getBean(japProperties.getSocialUserService());

        if(ObjectUtil.isNull(social))
            throw new NoSuchBeanDefinitionException("SocialStrategy 需要的japUserService未指定");

        return new SocialStrategy(social,japProperties);
    }

}

