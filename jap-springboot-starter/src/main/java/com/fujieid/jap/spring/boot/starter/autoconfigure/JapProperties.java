package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.core.config.JapConfig;
import com.fujieid.jap.oauth2.OAuthConfig;
import com.fujieid.jap.oidc.OidcConfig;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.social.SocialConfig;
import com.fujieid.jap.sso.config.JapSsoConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "jap")
@Getter
@Setter
public class JapProperties{

    @NestedConfigurationProperty
    private JapConfig basic = new JapConfig();

    @NestedConfigurationProperty
    private JapSsoConfig sso = new JapSsoConfig();

    @NestedConfigurationProperty
    private SimpleConfig simple = new SimpleConfig();

    private Map<String, SocialConfig> social = new HashMap<>();

    private Map<String, OAuthConfig> oauth = new HashMap<>();

    private Map<String, OidcConfig> oidc = new HashMap<>();

    @NestedConfigurationProperty
    private CacheProperties cache;

    private Class<?> simpleUserService;

    private Class<?> socialUserService;

    private Class<?> oauth2UserService;

    private Class<?> oidcUserService;


    public void setSso(JapSsoConfig sso) {
        // 设置sso的时候要把该sso配置到JapConfig对象中
        this.getBasic().setSsoConfig(sso);
        this.sso = sso;
    }
}
