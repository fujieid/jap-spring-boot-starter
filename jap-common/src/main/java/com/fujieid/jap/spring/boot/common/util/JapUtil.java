package com.fujieid.jap.spring.boot.common.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.core.strategy.AbstractJapStrategy;
import com.fujieid.jap.spring.boot.common.autoconfigure.JapUserServiceType;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JapUtil {
    /**
     * 提供3种方式获得JapUserService：<br/>
     * 1.application.properties中配置service的binary name;<br/>
     * 2.service实现类上注解@Service，并指定名称，如{@code JapServiceType.SIMPLE}。<br/>
     * 3.（考虑是否实现）以SimpleStrategy为例，将service类的名称命名为{@code SimpleUserService}或{@code SimpleUserServiceImpl}<br/>
     * 第一种方式优先级高
     * @param applicationContext applicationContext
     * @param japUserServiceType application.properties中配置的service的binary name，也即包全名
     * @param clazz service的class对象
     * @return
     */
    public static JapUserService getUserService(ApplicationContext applicationContext,
                                                String japUserServiceType, Class<?> clazz){
        if (!ObjectUtil.isNull(clazz) && !ClassUtil.isAssignable(JapUserService.class,clazz)) {
            throw new JapException("Unsupported parameter type, please use " + ClassUtil.getClassName(JapUserService.class, true) + ", an implement of JapUserService");
        }
        try {
            return applicationContext.containsBean(japUserServiceType) ?
                    (JapUserService) applicationContext.getBean(japUserServiceType) :
                    (JapUserService) applicationContext.getBean(clazz);
        } catch (Exception e){
            e.printStackTrace();
            throw new JapException("没有相应的userService!");
        }

    }
    public static JapResponse authenticate(AbstractJapStrategy abstractJapStrategy,
                                       AuthenticateConfig authenticateConfig){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(requestAttributes)) {
            return JapResponse.error(400, "当前请求（线程）不存在request上下文");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        return authenticate(abstractJapStrategy, authenticateConfig, request, response);
    }

    public static JapResponse authenticate(AbstractJapStrategy abstractJapStrategy,
                                       AuthenticateConfig authenticateConfig,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        return abstractJapStrategy.authenticate(authenticateConfig, request, response);
    }
}
