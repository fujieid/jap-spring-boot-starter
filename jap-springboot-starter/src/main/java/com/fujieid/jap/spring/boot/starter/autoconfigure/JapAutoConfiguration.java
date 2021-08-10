package com.fujieid.jap.spring.boot.starter.autoconfigure;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapException;
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
@EnableConfigurationProperties(value = {JapProperties.class})
public class JapAutoConfiguration {
    private final Logger logger = LoggerFactory.getLogger(JapAutoConfiguration.class);

    /**
     * 保证所有的strategy注入bean容器后才创建JapStrategyFactory实例
     * @return japStrategyFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public JapStrategyFactory japStrategyFactory(JapProperties japProperties,
                                                 ApplicationContext applicationContext,
                                                 SimpleStrategy simpleStrategy,
                                                 SocialStrategy socialStrategy,
                                                 Oauth2Strategy oauth2Strategy,
                                                 OidcStrategy oidcStrategy){
//        japProperties.getSocial().setJustAuthConfig(japProperties.getAuth());

        Strategy.SIMPLE.setAuthenticateConfig(japProperties.getSimple());
        Strategy.SOCIAL.setAuthenticateConfig(japProperties.getSocial());
        Strategy.OAUTH2.setAuthenticateConfig(japProperties.getOauth());
        Strategy.OIDC.setAuthenticateConfig(japProperties.getOidc());

        return new JapStrategyFactory(japProperties,applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext,
                                         JapProperties japProperties,
                                         JapCache japCache) {
        try {
            //两种方式指定JapUserService：@Service(JapServiceType.SOCIAL)；在application.properties中指定类全名
            JapUserService simple = getUserService(applicationContext,JapServiceType.SIMPLE,japProperties.getSimpleUserService());
            // TODO: 2021/7/23 cache是否添加支持自定义，不一定采用默认的？
            return new SimpleStrategy(simple, japProperties, japCache);
        } catch (Exception e) {
            logger.warn("尚未指定simpleStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new SimpleStrategy(new DefaultJapUserService(),japProperties,japCache);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext,
                                         JapProperties japProperties,
                                         JapCache japCache) {
        try {
            JapUserService social = getUserService(applicationContext,JapServiceType.SOCIAL,japProperties.getSocialUserService());
            return new SocialStrategy(social,japProperties, japCache);
        } catch (Exception e){
            logger.warn("尚未指定socialStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new SocialStrategy(new DefaultJapUserService(),japProperties,japCache);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public Oauth2Strategy oauth2Strategy(ApplicationContext applicationContext,
                                         JapProperties japProperties,
                                         JapCache japCache) {
        try{
            JapUserService oauth2 = getUserService(applicationContext,JapServiceType.OAUTH2,japProperties.getOauth2UserService());
            return new Oauth2Strategy(oauth2,japProperties, japCache);
        } catch (Exception e){
            logger.warn("尚未指定oauth2Strategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new Oauth2Strategy(new DefaultJapUserService(),japProperties,japCache);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public OidcStrategy oidcStrategy(ApplicationContext applicationContext,
                                     JapProperties japProperties,
                                     JapCache japCache) {
        try{
            JapUserService oidc = getUserService(applicationContext,JapServiceType.OIDC,japProperties.getOidcUserService());
            return new OidcStrategy(oidc,japProperties, japCache);
        } catch (Exception e){
            logger.warn("尚未指定oidcStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new OidcStrategy(new DefaultJapUserService(), japProperties, japCache);
        }
    }

    /**
     * 提供两种方式获得JapUserService：application.properties中配置service的binary name;
     * <br/>service实现类上注解@Service，并指定名称如{@code JapServiceType.SIMPLE}。
     * <br/>第一种方式优先级高
     * @param applicationContext
     * @param japServiceType
     * @param clazz
     * @return
     */
    private JapUserService getUserService(ApplicationContext applicationContext,
                                          String japServiceType, Class<?> clazz){
        if (!ObjectUtil.isNull(clazz) && !ClassUtil.isAssignable(JapUserService.class,clazz)) {
            throw new JapException("Unsupported parameter type, please use " + ClassUtil.getClassName(JapUserService.class, true) + ", a subclass of AuthenticateConfig");
        }
        return applicationContext.containsBean(japServiceType) ?
                (JapUserService) applicationContext.getBean(japServiceType) :
                (JapUserService) applicationContext.getBean(clazz);
    }



}

