package com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.starter.support.cache.RedisJapCache;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import com.fujieid.jap.spring.boot.starter.autoconfigure.CacheProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapUserServiceType;
import com.fujieid.jap.spring.boot.starter.support.util.JapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;


@Configuration
@Slf4j
public class SimpleAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SimpleStrategy simpleStrategy(ApplicationContext applicationContext,
                                         JapTemplate japTemplate,
                                         JapProperties japProperties,
                                         JapCache japCache) {
        SimpleStrategy simpleStrategy = null;
        try {
            //两种方式指定JapUserService：@Service(JapServiceType.SOCIAL)；在application.properties中指定类全名
            JapUserService simple = JapUtil.getUserService(applicationContext, JapUserServiceType.SIMPLE,japProperties.getSimpleUserService());
            simpleStrategy = new SimpleStrategy(simple, japProperties.getBasic(), japCache);
            return simpleStrategy;
        } catch (Exception e) {
            log.warn("尚未指定simpleStrategy的JapUserService。若需采用该策略进行认证，请指定JapUserService实现类");
            simpleStrategy = new SimpleStrategy(null,japProperties.getBasic(),japCache);
            return simpleStrategy;
        }finally {
            japTemplate.setSimpleStrategy(simpleStrategy);
        }
    }

    @Bean
    public JapCache japCache(JapProperties japProperties,
                             BeanFactory beanFactory,
                             RedisTemplate<String, Serializable> redisTemplate){
        JapCache japCache = new JapLocalCache();
        CacheProperties.CacheType type = japProperties.getCache().getType();

        if (type.equals(CacheProperties.CacheType.REDIS)) japCache = new RedisJapCache(redisTemplate,japProperties.getCache());
        else if (type.equals(CacheProperties.CacheType.CUSTOM)) {
            // TODO: 2021/8/9 实现自定义cache
//            ((DefaultListableBeanFactory)beanFactory).registerSingleton("customJapCache",);
        }
        return japCache;
    }


}
