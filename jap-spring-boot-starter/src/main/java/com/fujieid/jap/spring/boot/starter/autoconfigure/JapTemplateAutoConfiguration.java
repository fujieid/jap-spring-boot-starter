package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.spring.boot.starter.JapTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    public JapTemplate japTemplate(){
        return new JapTemplate();
    }

}