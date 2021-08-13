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
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Configuration
@EnableConfigurationProperties(value = {JapProperties.class})
@Slf4j
public class JapAutoConfiguration {

    /**
     * 保证所有的strategy注入bean容器后才创建JapStrategyFactory实例
     * @return japStrategyFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public JapTemplate japStrategyFactory(JapProperties japProperties,
                                          ApplicationContext applicationContext,
                                          SimpleStrategy simpleStrategy,
                                          SocialStrategy socialStrategy,
                                          Oauth2Strategy oauth2Strategy,
                                          OidcStrategy oidcStrategy){
        return new JapTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext,
                                         JapProperties japProperties,
                                         JapCache japCache) {
        try {
            //两种方式指定JapUserService：@Service(JapServiceType.SOCIAL)；在application.properties中指定类全名
            JapUserService simple = getUserService(applicationContext, JapUserServiceType.SIMPLE,japProperties.getSimpleUserService());
            return new SimpleStrategy(simple, japProperties.getBasic(), japCache);
        } catch (Exception e) {
            log.warn("尚未指定simpleStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new SimpleStrategy(null,japProperties.getBasic(),japCache);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext,
                                         JapProperties japProperties,
                                         JapCache japCache,
                                         AuthStateCache authStateCache) {
        try {
            JapUserService social = getUserService(applicationContext, JapUserServiceType.SOCIAL, japProperties.getSocialUserService());
            SocialStrategy socialStrategy = new SocialStrategy(social, japProperties.getBasic(), japCache);
            //通过反射注入cache
            Field stateCacheFiled = SocialStrategy.class.getDeclaredField("authStateCache");
            stateCacheFiled.setAccessible(true);
            stateCacheFiled.set(socialStrategy, authStateCache);

            return socialStrategy;
        } catch (Exception e){
            log.warn("尚未指定socialStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new SocialStrategy(null,japProperties.getBasic(),japCache);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public Oauth2Strategy oauth2Strategy(ApplicationContext applicationContext,
                                         JapProperties japProperties,
                                         JapCache japCache) {
        try{
            JapUserService oauth2 = getUserService(applicationContext, JapUserServiceType.OAUTH2,japProperties.getOauth2UserService());
            return new Oauth2Strategy(oauth2, japProperties.getBasic(), japCache);
        } catch (Exception e){
            log.warn("尚未指定oauth2Strategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new Oauth2Strategy(null, japProperties.getBasic(), japCache);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public OidcStrategy oidcStrategy(ApplicationContext applicationContext,
                                     JapProperties japProperties,
                                     JapCache japCache) {
        try{
            JapUserService oidc = getUserService(applicationContext, JapUserServiceType.OIDC,japProperties.getOidcUserService());
            return new OidcStrategy(oidc,japProperties.getBasic(), japCache);
        } catch (Exception e){
            log.warn("尚未指定oidcStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            return new OidcStrategy(null, japProperties.getBasic(), japCache);
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
            throw new JapException("Unsupported parameter type, please use " + ClassUtil.getClassName(JapUserService.class, true) + ", an implement of JapUserService");
        }
        return applicationContext.containsBean(japServiceType) ?
                (JapUserService) applicationContext.getBean(japServiceType) :
                (JapUserService) applicationContext.getBean(clazz);
    }
}