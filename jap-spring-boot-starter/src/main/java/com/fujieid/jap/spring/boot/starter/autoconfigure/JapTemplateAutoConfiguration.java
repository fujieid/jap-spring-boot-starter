package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import com.fujieid.spring.boot.japoauth2springbootstarter.Oauth2Operations;
import com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure.Oauth2Properties;
import com.fujieid.spring.boot.japsocialspringbootstarter.SocialOperations;
import com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure.SocialProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class JapTemplateAutoConfiguration {

    /**
     * 保证所有的strategy注入bean容器后才创建JapStrategyFactory实例
     * @return japStrategyFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public JapTemplate japTemplate(ApplicationContext applicationContext,
                                   Oauth2Operations oauth2Operations,
                                   SocialOperations socialOperations){
        return new JapTemplate(applicationContext, oauth2Operations, socialOperations);
    }

    @Bean
    @ConditionalOnClass({Oauth2Operations.class})
    public Oauth2Operations oauth2GrantOperations(Oauth2Strategy oauth2Strategy, Oauth2Properties oauth2Properties){
        return new Oauth2Operations(oauth2Strategy, oauth2Properties);
    }

    @Bean
    @ConditionalOnClass({SocialOperations.class})
    public SocialOperations socialCacheProperties(SocialStrategy socialStrategy, SocialProperties socialProperties){
        return new SocialOperations(socialStrategy, socialProperties);
    }

}