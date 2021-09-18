package com.fujieid.jap.spring.boot.common.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({JapBasicProperties.class, CacheProperties.class})
public class JapBasicAutoConfiguration {

}
