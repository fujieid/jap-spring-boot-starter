package com.fujieid.jap.spring.boot.starter;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.spring.boot.starter.autoconfigure.DefaultJapUserService;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

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
     *
     * @param strategy {@code Strategy.SIMPLE}，{@code Strategy.SOCIAL}，{@code Strategy.OAUTH2}，{@code Strategy.OIDC}
     */
    public JapResponse authenticate(Strategy strategy) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(requestAttributes)) {
            return JapResponse.error(400, "当前请求（线程）不存在request上下文");
        }

        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        return authenticate(getStrategy(strategy),strategy.getAuthenticateConfig(),request,response);
    }

    public JapResponse authenticate(Strategy strategy, HttpServletRequest request, HttpServletResponse response) {
        return authenticate(getStrategy(strategy),strategy.getAuthenticateConfig(),request,response);
    }

    /**
     * 重新指定相应策略类的japUserService实现类，并进行授权
     * <br/>注意！会替换原有的JapUserStrategy实例。
     * @param strategy @param strategy {@code Strategy.SIMPLE}，{@code Strategy.SOCIAL}，{@code Strategy.OAUTH2}，{@code Strategy.OIDC}
     * @param japUserService 指定的japUserService实现类，会将相应策略类原有的覆盖japUserService对象替换
     */
    public JapResponse authenticate(Strategy strategy, JapUserService japUserService){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(requestAttributes)) {
            return JapResponse.error(400, "当前请求（线程）不存在request上下文");
        }

        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        AbstractJapStrategy abstractJapStrategy = getStrategy(strategy);
        setJapUserService(abstractJapStrategy,japUserService);
        return authenticate(abstractJapStrategy, strategy.getAuthenticateConfig(), request,response);
    }

    private <T extends AuthenticateConfig> JapResponse authenticate(AbstractJapStrategy abstractJapStrategy,
                                                                    T authenticateConfig,
                                                                    HttpServletRequest request, HttpServletResponse response) {
        if (ObjectUtil.isNull(abstractJapStrategy))
            return JapResponse.error(500,"no abstractJapStrategy in applicationContext correspond to specified Strategy: "+abstractJapStrategy);

        // TODO: 2021/8/9 每次授权的时候都会检查是否有service，似乎这样会很慢
        try {
            Field field = AbstractJapStrategy.class.getDeclaredField("japUserService");
            field.setAccessible(true);
            Object o = field.get(abstractJapStrategy);
            if (o instanceof DefaultJapUserService ||ObjectUtil.isNull(o))
                return JapResponse.error(500,
                        abstractJapStrategy.getClass().getSimpleName()+"没有指定相应的japUserService");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return abstractJapStrategy.authenticate(authenticateConfig, request, response);
    }


    private AbstractJapStrategy getStrategy(Strategy strategy) {
        //获取bean容器中的strategy。首先用bean的名称查找，如果没有则根据类型查找
        return this.applicationContext.containsBean(strategy.getStrategyType()) ?
                (AbstractJapStrategy) this.applicationContext.getBean(strategy.getStrategyType()) :
                (AbstractJapStrategy) this.applicationContext.getBean(strategy.getStrategy());
    }

    /**
     * 设置strategy的userService实例。若已经设置过，不允许重新设置。
     */
    private void setJapUserService(AbstractJapStrategy abstractJapStrategy,JapUserService japUserService){
        try {
            Field field = AbstractJapStrategy.class.getDeclaredField("japUserService");
            field.setAccessible(true);
            Object o = field.get(abstractJapStrategy);
            if (!(ObjectUtil.isNull(o))&&!(o instanceof DefaultJapUserService))
                throw new Exception("当前strategy对象已指定JapUserService实现类");
            field.set(abstractJapStrategy,japUserService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
