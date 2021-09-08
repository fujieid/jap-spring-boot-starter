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
        // TODO: 2021/8/29 需要模块化的方式引入每一个strategy，那么这里的代码就需要改变，
        //  不用等到所有的strategy都注入了才引用，而只需要japProperties和applicationContext
        return new JapTemplate();
    }

}