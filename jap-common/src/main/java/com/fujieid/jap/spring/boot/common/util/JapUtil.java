package com.fujieid.jap.spring.boot.common.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.exception.JapException;
import org.springframework.context.ApplicationContext;

public class JapUtil {
    public static String STRATEGY_NO_USERSERVICE = "{} 没有指定相应的JapUserService。若不使用此策略，请移除maven中相应依赖";
    /**
     * @param applicationContext applicationContext
     * @param japUserServiceType application.properties中配置的service的binary name，也即包全名
     * @param clazz service的class对象
     * @return JapUserService
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
