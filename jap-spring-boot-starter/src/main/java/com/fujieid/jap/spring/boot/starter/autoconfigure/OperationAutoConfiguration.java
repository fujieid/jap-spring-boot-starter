package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.SimpleProperties;
import com.fujieid.jap.spring.boot.starter.operations.Oauth2Operations;
import com.fujieid.jap.spring.boot.starter.operations.OidcOperations;
import com.fujieid.jap.spring.boot.starter.operations.SimpleOperations;
import com.fujieid.jap.spring.boot.starter.operations.SocialOperations;
import com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure.Oauth2Properties;
import com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure.OidcProperties;
import com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure.SocialProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@Slf4j
public class OperationAutoConfiguration {

    @ConditionalOnClass({SimpleStrategy.class})
    static class Simple{
        @Bean
        @ConditionalOnMissingBean
        public SimpleOperations simpleOperations(ApplicationContext applicationContext, SimpleProperties simpleProperties){
            return new SimpleOperations(applicationContext.getBean(SimpleStrategy.class), simpleProperties);
        }
    }

    @ConditionalOnClass({SocialStrategy.class})
    static class Social{
        @Bean
        @ConditionalOnMissingBean
        public SocialOperations socialOperations(ApplicationContext applicationContext, SocialProperties socialProperties){
            return new SocialOperations(applicationContext.getBean(SocialStrategy.class), socialProperties);
        }
    }

    @ConditionalOnClass({Oauth2Strategy.class})
    static class Oauth2{
        @Bean
        @ConditionalOnMissingBean
        public Oauth2Operations oauth2Operations(ApplicationContext applicationContext, Oauth2Properties oauth2Properties){
            return new Oauth2Operations(applicationContext.getBean(Oauth2Strategy.class), oauth2Properties);
        }
    }

    @ConditionalOnClass({OidcStrategy.class})
    static class Oidc{
        @Bean
        @ConditionalOnMissingBean
        public OidcOperations oidcOperations(ApplicationContext applicationContext, OidcProperties oidcProperties){
            return new OidcOperations(applicationContext.getBean(OidcStrategy.class),oidcProperties);
        }
    }
}
