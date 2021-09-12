package com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.oauth2.OAuthConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "jap")
@Getter
@Setter
public class Oauth2Properties {

    private List<OAuthConfig> oauth2 = new ArrayList<>();

    private Class<? extends JapUserService> oauth2UserService;

}
