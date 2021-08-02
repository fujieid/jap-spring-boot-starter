package com.fujieid.jap.spring.boot.starter.autoconfigure;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.config.JapConfig;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.oauth2.OAuthConfig;
import com.fujieid.jap.oidc.OidcConfig;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.social.SocialConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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

    private Class<?> simpleUserService;

    private Class<?> socialUserService;

    private Class<?> oauth2UserService;

    private Class<?> oidcUserService;

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

    public Class<?> getSimpleUserService() {
        return simpleUserService;
    }

    public void setSimpleUserService(Class<?> simpleUserService) {
        if(!ClassUtil.isAssignable(JapUserService.class, simpleUserService))
            throw new JapException(simpleUserService.getName()+"应为接口JapUserService的实现类");
        this.simpleUserService = simpleUserService;
    }

    public Class<?> getSocialUserService() {
        return socialUserService;
    }

    public void setSocialUserService(Class<?> socialUserService) {
        if(!ClassUtil.isAssignable(JapUserService.class, socialUserService))
            throw new JapException(socialUserService.getName()+"应为接口JapUserService的实现类");
        this.socialUserService = socialUserService;
    }

    public Class<?> getOauth2UserService() {
        return oauth2UserService;
    }

    public void setOauth2UserService(Class<?> oauth2UserService) {
        if(!ClassUtil.isAssignable(JapUserService.class, oauth2UserService))
            throw new JapException(oauth2UserService.getName()+"应为接口JapUserService的实现类");
        this.oauth2UserService = oauth2UserService;
    }

    public Class<?> getOidcUserService() {
        return oidcUserService;
    }

    public void setOidcUserService(Class<?> oidcUserService) {
        if(!ClassUtil.isAssignable(JapUserService.class, oidcUserService))
            throw new JapException(oidcUserService.getName()+"应为接口JapUserService的实现类");
        this.oidcUserService = oidcUserService;
    }
}
