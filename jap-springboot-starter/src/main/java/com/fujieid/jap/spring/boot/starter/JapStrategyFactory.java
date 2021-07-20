package com.fujieid.jap.spring.boot.starter;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.core.config.JapConfig;
import com.fujieid.jap.core.context.JapContext;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.core.store.SessionJapUserStore;
import com.fujieid.jap.core.store.SsoJapUserStore;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JapStrategyFactory {

    private final JapProperties japProperties;
    private final JapCache japCache;

    public JapStrategyFactory(JapProperties japProperties){
        this.japCache = new JapLocalCache();
        this.japProperties=japProperties;
    }
    public JapResponse authenticate(Strategy strategy, JapUserService japUserService,
                                    HttpServletRequest request, HttpServletResponse response){
        JapConfig japConfig = this.japProperties;
        JapUserStore japUserStore =
                japConfig.isSso()
                        ? new SsoJapUserStore(japUserService, japConfig.getSsoConfig()) : new SessionJapUserStore();
//        JapContext japContext = new JapContext(japUserStore, this.japCache,japConfig);
        try {
            AbstractJapStrategy abstractJapStrategy =
                    (AbstractJapStrategy) strategy
                            .getStrategy()
                            .getConstructor(JapUserService.class, JapConfig.class, JapCache.class)
                            .newInstance(japUserService,this.japProperties, this.japCache);

            return abstractJapStrategy.authenticate(strategy.getConfig(), request, response);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public <T extends AbstractJapStrategy> T create(Class<T> tClass ,JapUserService japUserService){
        try{
            return tClass
                    .getConstructor(JapUserService.class, JapConfig.class)
                    .newInstance(japUserService, this.japProperties);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
