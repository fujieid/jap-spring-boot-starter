package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.JapStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {JapProperties.class, AuthProperties.class})
public class JapAutoConfiguration {
    private final Logger logger = LoggerFactory.getLogger(JapAutoConfiguration.class);

    /**
     * 保证所有的strategy注入bean后才创建JapStrategyFactory实例
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public JapStrategyFactory japStrategyFactory(JapProperties japProperties,
                                                 AuthProperties authProperties,
                                                 ApplicationContext applicationContext,
                                                 SimpleStrategy simpleStrategy,
                                                 SocialStrategy socialStrategy,
                                                 Oauth2Strategy oauth2Strategy,
                                                 OidcStrategy oidcStrategy){
        japProperties.getSocial().setJustAuthConfig(authProperties);

        Strategy.SIMPLE.setAuthenticateConfig(japProperties.getSimple());
        Strategy.SOCIAL.setAuthenticateConfig(japProperties.getSocial());
        Strategy.OAUTH2.setAuthenticateConfig(japProperties.getOauth());
        Strategy.OIDC.setAuthenticateConfig(japProperties.getOidc());

        return new JapStrategyFactory(japProperties,applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext, JapProperties japProperties) {
        try {
            //两种方式指定JapUserService：@Service(JapServiceType.SOCIAL)；在application.properties中指定类全名
            JapUserService simple = applicationContext.containsBean(JapServiceType.SIMPLE) ?
                    (JapUserService) applicationContext.getBean(JapServiceType.SIMPLE) :
                    (JapUserService) applicationContext.getBean(japProperties.getSimpleUserService());

            // TODO: 2021/7/23 cache是否添加支持自定义，不一定采用默认的？
            return new SimpleStrategy(simple, japProperties, new JapLocalCache());
        } catch (Exception e) {
            logger.warn("没有指定simpleStrategy的JapUserService");
            return new SimpleStrategy(new DefaultJapUserService(),japProperties,new JapLocalCache());
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext, JapProperties japProperties) {
        try {
            JapUserService social = applicationContext.containsBean(JapServiceType.SOCIAL) ?
                    (JapUserService) applicationContext.getBean(JapServiceType.SOCIAL) :
                    (JapUserService) applicationContext.getBean(japProperties.getSocialUserService());
            return new SocialStrategy(social,japProperties, new JapLocalCache());
        } catch (Exception e){
            logger.warn("没有指定socialStrategy的JapUserService");
            return new SocialStrategy(new DefaultJapUserService(),japProperties,new JapLocalCache());
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public Oauth2Strategy oauth2Strategy(ApplicationContext applicationContext, JapProperties japProperties) {
        try{
            JapUserService oauth2 = applicationContext.containsBean(JapServiceType.OAUTH2) ?
                    (JapUserService) applicationContext.getBean(JapServiceType.OAUTH2) :
                    (JapUserService) applicationContext.getBean(japProperties.getOauth2UserService());
            return new Oauth2Strategy(oauth2,japProperties, new JapLocalCache());
        } catch (Exception e){
            logger.warn("没有指定oauth2Strategy的JapUserService");
            return new Oauth2Strategy(new DefaultJapUserService(),japProperties,new JapLocalCache());
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public OidcStrategy oidcStrategy(ApplicationContext applicationContext, JapProperties japProperties) {
        try{
            JapUserService oidc = applicationContext.containsBean(JapServiceType.OIDC) ?
                    (JapUserService) applicationContext.getBean(JapServiceType.OIDC) :
                    (JapUserService) applicationContext.getBean(japProperties.getOidcUserService());
            return new OidcStrategy(oidc,japProperties, new JapLocalCache());
        } catch (Exception e){
            logger.warn("没有指定oauth2Strategy的JapUserService");
            return new OidcStrategy(new DefaultJapUserService(),japProperties,new JapLocalCache());
        }

    }

    // TODO: 2021/7/23 japCache是否需要支持自定义，比如加入redisTemplate
    @Bean
    public JapCache japCache(){
        return new JapLocalCache();
    }

}

