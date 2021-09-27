package com.fujieid.jap.spring.boot.starter;

import com.fujieid.jap.spring.boot.starter.operations.Oauth2Operations;
import com.fujieid.jap.spring.boot.starter.operations.OidcOperations;
import com.fujieid.jap.spring.boot.starter.operations.SimpleOperations;
import com.fujieid.jap.spring.boot.starter.operations.SocialOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public class JapTemplate {
    private final ApplicationContext applicationContext;

    public JapTemplate(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public SimpleOperations opsForSimple() {
        return applicationContext.getBean(SimpleOperations.class);
    }

    public SocialOperations opsForSocial(){
        return applicationContext.getBean(SocialOperations.class);
    }

    public Oauth2Operations opsForOauth2(){
        return applicationContext.getBean(Oauth2Operations.class);
    }

    public OidcOperations opsForOidc(){
        return applicationContext.getBean(OidcOperations.class);
    }
}
