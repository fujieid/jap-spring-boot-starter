package com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure;

import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.JapStrategyFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = JapProperties.class)
public class JapAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public JapStrategyFactory japStrategyFactory(JapProperties japProperties){
        Strategy.SIMPLE.setConfig(japProperties.getSimple());
        Strategy.SOCIAL.setConfig(japProperties.getSocial());

        JapStrategyFactory japStrategyFactory = new JapStrategyFactory(japProperties);
        return japStrategyFactory;
    }

}

