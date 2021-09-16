package com.fujieid.jap.spring.boot.common.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.exception.JapException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class JapUtil {
    public static String STRATEGY_NO_USERSERVICE = "{} 没有指定相应的JapUserService";
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
        if (!ObjectUtil.isNull(clazz) && !ClassUtil.isAssignable(JapUserService.class, clazz)) {
            throw new JapException("不支持的参数类型, clazz, 请使用 " + ClassUtil.getClassName(JapUserService.class, true) + "的一个实现类");
        }
        return applicationContext.containsBean(japUserServiceType) ?
                (JapUserService) applicationContext.getBean(japUserServiceType) :
                (JapUserService) applicationContext.getBean(clazz);
    }
}
