package com.fujieid.jap.spring.boot.starter.autoconfigure;

import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;

public enum Strategy {
    SIMPLE("simpleStrategy", SimpleStrategy.class),
    SOCIAL("socialStrategy", SocialStrategy.class),
    OAUTH2("oauth2Strategy", Oauth2Strategy.class),
    OIDC("oidcStrategy", OidcStrategy.class)
    ;

    private String strategyType;
    private Class<?> strategy;
    private AuthenticateConfig config;

    Strategy(String strategyType, Class<?> strategy){
        this.strategyType=strategyType;
        this.strategy=strategy;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    public Class<?> getStrategy() {
        return strategy;
    }

    public void setStrategy(Class<?> strategy) {
        this.strategy = strategy;
    }

    public AuthenticateConfig getConfig() {
        return config;
    }

    public void setConfig(AuthenticateConfig config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "Strategy{" +
                "strategyType='" + strategyType + '\'' +
                ", strategy=" + strategy +
                ", config=" + config +
                '}';
    }
}
