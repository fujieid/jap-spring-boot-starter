package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.SimpleAutoConfiguration;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.SimpleProperties;
import com.fujieid.jap.spring.boot.starter.operations.Oauth2Operations;
import com.fujieid.jap.spring.boot.starter.operations.OidcOperations;
import com.fujieid.jap.spring.boot.starter.operations.SimpleOperations;
import com.fujieid.jap.spring.boot.starter.operations.SocialOperations;
import com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure.Oauth2AutoConfiguration;
import com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure.Oauth2Properties;
import com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure.OidcAutoConfiguration;
import com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure.OidcProperties;
import com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure.SocialAutoConfiguration;
import com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure.SocialProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@Slf4j
public class OperationAutoConfiguration {

    @ConditionalOnClass({SimpleStrategy.class})
    @AutoConfigureAfter(SimpleAutoConfiguration.class)
    static class Simple{
        @Bean
        @ConditionalOnMissingBean
        public SimpleOperations simpleOperations(SimpleStrategy simpleStrategy, SimpleProperties simpleProperties){
            return new SimpleOperations(simpleStrategy, simpleProperties);
        }
    }

    @ConditionalOnClass({SocialStrategy.class})
    @AutoConfigureAfter({SocialAutoConfiguration.class})
    static class Social{
        @Bean
        @ConditionalOnMissingBean
        public SocialOperations socialOperations(SocialStrategy socialStrategy, SocialProperties socialProperties){
            return new SocialOperations(socialStrategy, socialProperties);
        }
    }

    @ConditionalOnClass({Oauth2Strategy.class})
    @AutoConfigureAfter(Oauth2AutoConfiguration.class)
    static class Oauth2{
        @Bean
        @ConditionalOnMissingBean
        public Oauth2Operations oauth2Operations(Oauth2Strategy oauth2Strategy, Oauth2Properties oauth2Properties){
            return new Oauth2Operations(oauth2Strategy, oauth2Properties);
        }
    }

    @ConditionalOnClass({OidcStrategy.class})
    @AutoConfigureAfter(OidcAutoConfiguration.class)
    static class Oidc{
        @Bean
        @ConditionalOnMissingBean
        public OidcOperations oidcOperations(OidcStrategy oidcStrategy, OidcProperties oidcProperties){
            return new OidcOperations(oidcStrategy,oidcProperties);
        }
    }
}
