package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.spring.boot.starter.JapTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@Slf4j
public class JapTemplateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JapTemplate japTemplate(ApplicationContext applicationContext){
        return new JapTemplate(applicationContext);
    }

}