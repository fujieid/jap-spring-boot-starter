package com.fujieid.jap.spring.boot.common.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JapBasicProperties.class})
public class JapBasicAutoConfiguration {
}
