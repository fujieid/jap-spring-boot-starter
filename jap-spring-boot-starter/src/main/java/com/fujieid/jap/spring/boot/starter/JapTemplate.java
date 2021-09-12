package com.fujieid.jap.spring.boot.starter;

import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.SimpleProperties;
import com.fujieid.spring.boot.japoauth2springbootstarter.Oauth2Operations;
import com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure.OidcProperties;
import com.fujieid.spring.boot.japsocialspringbootstarter.SocialOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * 对每一种策略的authenticate方法封装。请确保调用下面的方法时当前线程的thread local中有request和response。
 */
// TODO: 2021/9/11 可以把类名改为JapAuthenticateTemplate
@Slf4j
public class JapTemplate {
    private ApplicationContext applicationContext;
    private Oauth2Operations oauth2Operations;
    private SocialOperations socialOperations;
    public JapTemplate(ApplicationContext applicationContext,
                       Oauth2Operations oauth2Operations,
                       SocialOperations socialOperations){
        this.applicationContext = applicationContext;
        this.oauth2Operations = oauth2Operations;
        this.socialOperations = socialOperations;
    }


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
        return JapUtil.authenticate(simpleStrategy, simpleProperties.getSimple());
    }

    public SocialOperations opsForSocial(){
        return this.socialOperations;
    }

    /**
     * oauth2的授权
     * @return
     */
    public Oauth2Operations opsForOauth2(){
        return this.oauth2Operations;
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
        return JapUtil.authenticate(oidcStrategy,oidcProperties.getOidc().get(platform));
    }

}
