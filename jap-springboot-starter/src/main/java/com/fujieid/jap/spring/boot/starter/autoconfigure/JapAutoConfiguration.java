package com.fujieid.jap.spring.boot.starter.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.JapStrategyFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@EnableConfigurationProperties(value = {JapProperties.class, AuthProperties.class})
public class JapAutoConfiguration {

    /**
     * 保证所有的strategy注入bean后才创建JapStrategyFactory实例
     * @param japProperties
     * @param authProperties
     * @param applicationContext
     * @param simpleStrategy
     * @param socialStrategy
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public JapStrategyFactory japStrategyFactory(JapProperties japProperties, AuthProperties authProperties,
                                                 ApplicationContext applicationContext,
                                                 SimpleStrategy simpleStrategy, SocialStrategy socialStrategy){
        japProperties.getSocial().setJustAuthConfig(authProperties);

        Strategy.SIMPLE.setConfig(japProperties.getSimple());
        Strategy.SOCIAL.setConfig(japProperties.getSocial());
        Strategy.OAUTH2.setConfig(japProperties.getOauth());
        Strategy.OIDC.setConfig(japProperties.getOidc());

        return new JapStrategyFactory(japProperties,applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext, JapProperties japProperties) {
        //两种方式指定JapUserService：@Service(JapServiceType.SOCIAL)；在application.properties中指定类全名
        JapUserService simple = applicationContext.containsBean(JapServiceType.SIMPLE) ?
                (JapUserService) applicationContext.getBean(JapServiceType.SIMPLE) :
                (JapUserService) applicationContext.getBean(japProperties.getSimpleUserService());

        if(ObjectUtil.isNull(simple))
            throw new NoSuchBeanDefinitionException("SimpleStrategy 需要的japUserService未指定");

        // TODO: 2021/7/23 cache是否添加支持自定义，不一定采用默认的？
        return new SimpleStrategy(simple, japProperties, new JapLocalCache());
    }

    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext, JapProperties japProperties) {
        JapUserService social = applicationContext.containsBean(JapServiceType.SOCIAL) ?
                (JapUserService) applicationContext.getBean(JapServiceType.SOCIAL) :
                (JapUserService) applicationContext.getBean(japProperties.getSocialUserService());

        if(ObjectUtil.isNull(social))
            throw new NoSuchBeanDefinitionException("SocialStrategy 需要的japUserService未指定");


        return new SocialStrategy(social,japProperties, new JapLocalCache());
    }

    // TODO: 2021/7/23 japCache是否需要支持自定义，比如加入redisTemplate
    @Bean
    public JapCache japCache(){
        return new JapLocalCache();
    }

    // TODO: 2021/7/23 完成剩下两个strategy的bean实例的创建

}

