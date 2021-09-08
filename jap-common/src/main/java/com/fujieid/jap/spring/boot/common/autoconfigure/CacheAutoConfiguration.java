package com.fujieid.jap.spring.boot.common.autoconfigure;

import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.core.store.SessionJapUserStore;
import com.fujieid.jap.spring.boot.common.cache.RedisJapCache;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

@Configuration
@EnableConfigurationProperties({CacheProperties.class})
public class CacheAutoConfiguration {
    /**
     * 这种自定义泛型的RedisTemplate需要自己创建
     */
    @Bean
    public RedisTemplate<String, Serializable> serializableRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, JapUser> japUserRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String,JapUser> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }


    @Bean
    public JapCache japCache(CacheProperties cacheProperties,
                             BeanFactory beanFactory,
                             RedisTemplate<String, Serializable> redisTemplate){
        JapCache japCache = new JapLocalCache();
        CacheType type = cacheProperties.getTokenCacheType();

        if (type.equals(CacheType.REDIS)){
            japCache = new RedisJapCache(redisTemplate,cacheProperties);
        } else if (type.equals(CacheType.CUSTOM)) {
            // TODO: 2021/8/9 实现自定义cache
//            ((DefaultListableBeanFactory)beanFactory).registerSingleton("customJapCache",);
        }
        return japCache;
    }

    // TODO: 2021/8/13 这个接口比较特殊，似乎并不欢迎用自己实现的方式。可以看到
    //  public AbstractJapStrategy(JapUserService japUserService, JapConfig japConfig, JapUserStore japUserStore, JapCache japCache)
    //  这个构造器没有被四种strategy的任何一个直接调用，所以如果要自定义JapUserStore得用到反射。
    //  暂时不考虑该接口
    @Bean
    public JapUserStore japUserStore(CacheProperties cacheProperties, RedisTemplate<String,JapUser> redisTemplate) {
        return new SessionJapUserStore();
    }


    /**
     * 参数中的redisTemplate是{@code RedisTemplate<String,String>}类型，这个类型和RedisTemplate类型一样，都是redistemplate依赖
     * 自动创建的bean，于是没有必要自己创建一个。而比如前面代码中我自己创建的{@code RedisTemplate<String,Serializable>}就和提到的这两种
     * RedisTemplate不是同一种类型，所以需要自己创建并注入bean。同时，注入bean不是按照名字，而是按照类型的，也就是虽然这个类中所有redisTemplate
     * 形参变量的名字都是redisTemplate，但是由于bean是单例模式，不会根据redisTemplate这个名字来寻找对应的bean，而是通过redisTemplate这个名字的
     * 类型来寻找！比如{@code RedisTemplate<String,String> redisTemplate}和{@code RedisTemplate<String,JapUser> redisTemplate}
     * 虽然实参变量名都是redisTemplate，但是最后bean容器注入那个单例是按照它们的类型来决定的。
     */

}
