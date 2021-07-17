package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.spring.boot.starter.JapStrategyFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {JapProperties.class, AuthProperties.class})
public class JapAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public JapStrategyFactory japStrategyFactory(JapProperties japProperties, AuthProperties authProperties){
        japProperties.getSocial().setJustAuthConfig(authProperties);
        Strategy.SIMPLE.setConfig(japProperties.getSimple());
        Strategy.SOCIAL.setConfig(japProperties.getSocial());

        return new JapStrategyFactory(japProperties);
    }

}

