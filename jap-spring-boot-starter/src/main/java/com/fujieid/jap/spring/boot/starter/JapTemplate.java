package com.fujieid.jap.spring.boot.starter;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.SimpleProperties;
import com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure.Oauth2Properties;
import com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure.OidcProperties;
import com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure.SocialProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JapTemplate {
    @Autowired
    ApplicationContext applicationContext;


    public JapResponse simple() {
        SimpleStrategy simpleStrategy;
        SimpleProperties simpleProperties;
        try {
            simpleStrategy = applicationContext.getBean(SimpleStrategy.class);
            simpleProperties = applicationContext.getBean(SimpleProperties.class);
        } catch (NoClassDefFoundError | BeansException e){
            e.printStackTrace();
            throw new JapException("no module: jap-simple-spring-boot-starter");
        }
        return authenticate(simpleStrategy, simpleProperties.getSimple());
    }

    public JapResponse social(String platform){
        SocialStrategy socialStrategy;
        SocialProperties socialProperties;
        try {
            socialStrategy = applicationContext.getBean(SocialStrategy.class);
            socialProperties = applicationContext.getBean(SocialProperties.class);
        } catch (NoClassDefFoundError | BeansException e){
            e.printStackTrace();
            throw new JapException("no module: jap-social-spring-boot-starter");
        }
        return authenticate(socialStrategy,socialProperties.getSocial().get(platform));

    }

    public JapResponse oauth(String platform){
        Oauth2Strategy oauth2Strategy;
        Oauth2Properties oauth2Properties;
        try {
            oauth2Strategy = applicationContext.getBean(Oauth2Strategy.class);
            oauth2Properties = applicationContext.getBean(Oauth2Properties.class);
        } catch (NoClassDefFoundError | BeansException e){
            e.printStackTrace();
            throw new JapException("no module: jap-oauth2-spring-boot-starter");
        }
        return authenticate(oauth2Strategy,oauth2Properties.getOauth2().get(platform));
    }

    public JapResponse oidc(String platform){
        OidcStrategy oidcStrategy;
        OidcProperties oidcProperties;
        try {
            oidcStrategy = applicationContext.getBean(OidcStrategy.class);
            oidcProperties = applicationContext.getBean(OidcProperties.class);
        } catch (NoClassDefFoundError | BeansException e){
            e.printStackTrace();
            throw new JapException("no module: jap-oidc-spring-boot-starter");
        }
        return authenticate(oidcStrategy,oidcProperties.getOidc().get(platform));
    }

    public <T extends AuthenticateConfig> JapResponse authenticate(AbstractJapStrategy abstractJapStrategy,
                                                                   T authenticateConfig){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(requestAttributes)) {
            return JapResponse.error(400, "当前请求（线程）不存在request上下文");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        return authenticate(abstractJapStrategy, authenticateConfig, request, response);
    }

    public <T extends AuthenticateConfig> JapResponse authenticate(AbstractJapStrategy abstractJapStrategy,
                                                                    T authenticateConfig,
                                                                    HttpServletRequest request, HttpServletResponse response) {
        return abstractJapStrategy.authenticate(authenticateConfig, request, response);
    }


}
