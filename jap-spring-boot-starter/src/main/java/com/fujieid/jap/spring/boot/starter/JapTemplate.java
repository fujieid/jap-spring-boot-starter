package com.fujieid.jap.spring.boot.starter;

import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.exception.JapOauth2Exception;
import com.fujieid.jap.core.exception.JapSocialException;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.SimpleOperations;
import com.fujieid.spring.boot.japoauth2springbootstarter.Oauth2Operations;
import com.fujieid.spring.boot.japoidcspringbootstarter.OidcOperations;
import com.fujieid.spring.boot.japsocialspringbootstarter.SocialOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.beans.Beans;

@Slf4j
public class JapTemplate {
    private final ApplicationContext applicationContext;

    public JapTemplate(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }


    public SimpleOperations opsForSimple() {
        try {
            return applicationContext.getBean(SimpleOperations.class);
        }catch (NoClassDefFoundError | BeansException e){
            throw new JapException(e.getMessage(),e.getCause());
        }
    }

    public SocialOperations opsForSocial(){
        try {
            return applicationContext.getBean(SocialOperations.class);
        } catch (NoClassDefFoundError | BeansException e) {
            throw new JapSocialException(e.getMessage(),e.getCause());
        }
    }

    public Oauth2Operations opsForOauth2(){
        try {
            return applicationContext.getBean(Oauth2Operations.class);
        } catch (NoClassDefFoundError | BeansException e){
            throw new JapOauth2Exception(e.getMessage(), e.getCause());
        }
    }

    public OidcOperations opsForOidc(){
        try {
            return applicationContext.getBean(OidcOperations.class);
        } catch (NoClassDefFoundError | BeansException e){
            throw new JapOauth2Exception(e.getMessage(), e.getCause());
        }
    }

}
