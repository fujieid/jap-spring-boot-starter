package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.SimpleOperations;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.SimpleProperties;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import com.fujieid.spring.boot.japoauth2springbootstarter.Oauth2Operations;
import com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure.Oauth2Properties;
import com.fujieid.spring.boot.japoidcspringbootstarter.OidcOperations;
import com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure.OidcProperties;
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

    @Bean
    @ConditionalOnMissingBean
    public JapTemplate japTemplate(ApplicationContext applicationContext){
        return new JapTemplate(applicationContext);
    }

}