package com.fujieid.jap.spring.boot.japsimplespringbootstarter;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.core.config.JapConfig;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.Strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JapStrategyFactory {
    private JapProperties japProperties;


    public JapStrategyFactory(JapProperties japProperties){
        this.japProperties=japProperties;

    }
    public JapResponse authenticate(Strategy strategy, JapUserService japUserService,
                                    HttpServletRequest request, HttpServletResponse response){
        try {
            AbstractJapStrategy abstractJapStrategy =
                    (AbstractJapStrategy) strategy.getStrategy()
                            .getConstructor(JapUserService.class, JapConfig.class)
                            .newInstance(japUserService,this.japProperties);

            JapResponse japResponse = abstractJapStrategy.authenticate(strategy.getConfig(), request, response);

            return japResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public <T extends AbstractJapStrategy> T create(Class<T> tClass ,JapUserService japUserService){
        try{
            T strategy =
                    (T) tClass
                            .getConstructor(JapUserService.class, JapConfig.class)
                            .newInstance(japUserService, this.japProperties);
            return strategy;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
