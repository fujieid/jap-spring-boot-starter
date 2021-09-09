package com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.social.SocialConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "jap")
@Getter
@Setter
public class SocialProperties {
    Map<String,SocialConfig> social = new HashMap<>();

    private Class<? extends JapUserService> socialUserService;
}