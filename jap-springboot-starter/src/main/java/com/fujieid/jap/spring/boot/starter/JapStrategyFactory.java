package com.fujieid.jap.spring.boot.starter;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.core.result.JapErrorCode;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JapStrategyFactory {

    private final JapProperties japProperties;
    private final JapCache japCache;
    private final ApplicationContext applicationContext;

    public JapStrategyFactory(JapProperties japProperties, ApplicationContext applicationContext){
        this.japCache = new JapLocalCache();
        this.japProperties=japProperties;
        this.applicationContext=applicationContext;
    }
    /**
     * 获取当前线程绑定的request和response。不是特别确定，但应该是没有线程问题的。
     * @param strategy
     * @return
     */
    public JapResponse authenticate(Strategy strategy) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(requestAttributes)) {
            return JapResponse.error(400, "当前请求（线程）不存在request上下文");
        }

        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        return authenticate(strategy,strategy.getConfig(),request,response);
    }

    public JapResponse authenticate(Strategy strategy, HttpServletRequest request, HttpServletResponse response) {
        return authenticate(strategy,strategy.getConfig(),request,response);
    }

    public <T extends AuthenticateConfig> JapResponse authenticate(Strategy strategy, T authenticateConfig, HttpServletRequest request, HttpServletResponse response) {
        //获取bean容器中的strategy。首先用bean的名称查找，如果没有则根据类型查找
        AbstractJapStrategy abstractJapStrategy =
                this.applicationContext.containsBean(strategy.getStrategyType()) ?
                 (AbstractJapStrategy) this.applicationContext.getBean(strategy.getStrategyType()) : (AbstractJapStrategy) this.applicationContext.getBean(strategy.getStrategy());
        if (ObjectUtil.isNull(abstractJapStrategy))
            return JapResponse.error(500,"no abstractJapStrategy in applicationContext correspond to specified Strategy: "+strategy);

        return abstractJapStrategy.authenticate(authenticateConfig, request, response);
    }


    public <T extends AbstractJapStrategy> T getStrategy(Class<T> strategyClass) {
        return this.applicationContext.getBean(strategyClass);
    }

}
