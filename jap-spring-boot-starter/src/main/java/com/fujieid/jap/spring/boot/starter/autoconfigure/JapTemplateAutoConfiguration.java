package com.fujieid.jap.spring.boot.starter.autoconfigure;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.SimpleProperties;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import com.fujieid.jap.spring.boot.starter.operations.SimpleOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Slf4j
public class JapTemplateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JapTemplate japTemplate(ApplicationContext applicationContext){
        return new JapTemplate(applicationContext);
    }

    @Configuration
    @Import({OperationAutoConfiguration.Simple.class,
            OperationAutoConfiguration.Social.class,
            OperationAutoConfiguration.Oauth2.class,
            OperationAutoConfiguration.Oidc.class})
    static class OpsConfig{
    }

}