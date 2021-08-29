package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import com.fujieid.jap.spring.boot.starter.support.util.JapUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public JapTemplate japTemplate(JapProperties japProperties,
                                   ApplicationContext applicationContext){
        // TODO: 2021/8/29 需要模块化的方式引入每一个strategy，那么这里的代码就需要改变，
        //  不用等到所有的strategy都注入了才引用，而只需要japProperties和applicationContext
        return new JapTemplate();
    }
}