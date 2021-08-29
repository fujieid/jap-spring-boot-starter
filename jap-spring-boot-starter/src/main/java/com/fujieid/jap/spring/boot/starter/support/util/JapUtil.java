package com.fujieid.jap.spring.boot.starter.support.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.exception.JapException;
import org.springframework.context.ApplicationContext;

public class JapUtil {
    /**
     * 提供3种方式获得JapUserService：<br/>
     * 1.application.properties中配置service的binary name;<br/>
     * 2.service实现类上注解@Service，并指定名称，如{@code JapServiceType.SIMPLE}。<br/>
     * 3.（考虑是否实现）以SimpleStrategy为例，将service类的名称命名为{@code SimpleUserService}或{@code SimpleUserServiceImpl}<br/>
     * 第一种方式优先级高
     * @param applicationContext applicationContext
     * @param japServiceType application.properties中配置的service的binary name，也即包全名
     * @param clazz service的class对象
     * @return
     */
    static public JapUserService getUserService(ApplicationContext applicationContext,
                                          String japServiceType, Class<?> clazz){
        if (!ObjectUtil.isNull(clazz) && !ClassUtil.isAssignable(JapUserService.class,clazz)) {
            throw new JapException("Unsupported parameter type, please use " + ClassUtil.getClassName(JapUserService.class, true) + ", an implement of JapUserService");
        }
        return applicationContext.containsBean(japServiceType) ?
                (JapUserService) applicationContext.getBean(japServiceType) :
                (JapUserService) applicationContext.getBean(clazz);
    }
}
