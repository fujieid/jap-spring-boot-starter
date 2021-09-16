package com.fujieid.jap.spring.boot.starter.operations;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractJapOperations {
    final protected String PLATFORM_NO_CORRESPOND_CONFIG = "{} 没有相应的配置";

    /**
     * 调用此方法需要确保当前线程有请求上下文
     * @param abstractJapStrategy
     * @param authenticateConfig
     * @return
     */
    protected JapResponse authenticate(AbstractJapStrategy abstractJapStrategy,
                                           AuthenticateConfig authenticateConfig){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(requestAttributes)) {
            return JapResponse.error(400, "当前请求（线程）不存在request上下文");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        return authenticate(abstractJapStrategy, authenticateConfig, request, response);
    }

    protected JapResponse authenticate(AbstractJapStrategy abstractJapStrategy,
                                           AuthenticateConfig authenticateConfig,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        return abstractJapStrategy.authenticate(authenticateConfig, request, response);
    }
}
