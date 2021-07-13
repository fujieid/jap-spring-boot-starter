package com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure;

import com.fujieid.jap.core.config.JapConfig;
import com.fujieid.jap.oauth2.OAuthConfig;
import com.fujieid.jap.oidc.OidcConfig;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.social.SocialConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jap")
public class JapProperties extends JapConfig {
    @NestedConfigurationProperty
    private SimpleConfig simple = new SimpleConfig();

    @NestedConfigurationProperty
    private SocialConfig social = new SocialConfig();

    @NestedConfigurationProperty
    private OAuthConfig oauth = new OAuthConfig();

    @NestedConfigurationProperty
    private OidcConfig oidc = new OidcConfig();

    public SimpleConfig getSimple() {
        return simple;
    }

    public void setSimple(SimpleConfig simple) {
        this.simple = simple;
    }

    public SocialConfig getSocial() {
        return social;
    }

    public void setSocial(SocialConfig social) {
        this.social = social;
    }

    public OAuthConfig getOauth() {
        return oauth;
    }

    public void setOauth(OAuthConfig oauth) {
        this.oauth = oauth;
    }

    public OidcConfig getOidc() {
        return oidc;
    }

    public void setOidc(OidcConfig oidc) {
        this.oidc = oidc;
    }
}
