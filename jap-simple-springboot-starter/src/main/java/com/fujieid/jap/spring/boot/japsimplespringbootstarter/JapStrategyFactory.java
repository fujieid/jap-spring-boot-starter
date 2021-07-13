package com.fujieid.jap.spring.boot.japsimplespringbootstarter;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.config.JapConfig;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.Strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JapStrategyFactory {

    private final JapProperties japProperties;

    public JapStrategyFactory(JapProperties japProperties){
        this.japProperties=japProperties;

    }
    public JapResponse authenticate(Strategy strategy, JapUserService japUserService,
                                    HttpServletRequest request, HttpServletResponse response){
        try {
            AbstractJapStrategy abstractJapStrategy =
                    (AbstractJapStrategy) strategy
                            .getStrategy()
                            .getConstructor(JapUserService.class, JapConfig.class)
                            .newInstance(japUserService,this.japProperties);

            return abstractJapStrategy.authenticate(strategy.getConfig(), request, response);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public <T extends AbstractJapStrategy> T create(Class<T> tClass ,JapUserService japUserService){
        try{
            return tClass
                    .getConstructor(JapUserService.class, JapConfig.class)
                    .newInstance(japUserService, this.japProperties);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
