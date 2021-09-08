package com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.oidc.OidcConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "jap")
@Getter
@Setter
public class OidcProperties {
    private Map<String, OidcConfig> oidc = new HashMap<>();

    private Class<? extends JapUserService> oidcUserService;
}
