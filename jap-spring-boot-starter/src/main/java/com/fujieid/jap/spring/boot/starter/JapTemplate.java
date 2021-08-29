package com.fujieid.jap.spring.boot.starter;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.oauth2.OAuthConfig;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcConfig;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialConfig;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
@Slf4j
public class JapTemplate {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private JapProperties japProperties;

    private SimpleStrategy simpleStrategy;
    private SocialStrategy socialStrategy;
    private Oauth2Strategy oauth2Strategy;
    private OidcStrategy oidcStrategy;



    // TODO: 2021/8/13 是否需要支持传入JapUserService参数?
    public JapResponse simple(){
        SimpleConfig simpleConfig = japProperties.getSimple();
        return authenticate(this.simpleStrategy,simpleConfig);
    }

    public JapResponse social(String platform){
        SocialConfig socialConfig = japProperties.getSocial().get(platform);
        return authenticate(socialStrategy,socialConfig);
    }

    public JapResponse oauth(String platform){
        OAuthConfig oAuthConfig = japProperties.getOauth().get(platform);
        return authenticate(oauth2Strategy,oAuthConfig);
    }

    public JapResponse oidc(String platform){
        OidcConfig oidcConfig = japProperties.getOidc().get(platform);
        return authenticate(oidcStrategy,oidcConfig);
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
        if (ObjectUtil.isNull(abstractJapStrategy))
            return JapResponse.error(500,"no abstractJapStrategy in applicationContext correspond to specified Strategy: "+abstractJapStrategy);
        return abstractJapStrategy.authenticate(authenticateConfig, request, response);
    }


    private <T> T getBean(ApplicationContext applicationContext,Class<T> clazz){
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e){
            throw new JapException("no bean named"+clazz.getSimpleName()+"found in application context");
        }

    }

}
