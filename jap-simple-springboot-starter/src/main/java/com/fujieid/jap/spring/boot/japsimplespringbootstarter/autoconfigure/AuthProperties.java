package com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure;

import me.zhyd.oauth.config.AuthConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jap.auth")
public class AuthProperties extends AuthConfig {
}
