package com.fujieid.jap.spring.boot.common.autoconfigure;

import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.cache.JapLocalCache;
import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.spring.boot.common.cache.RedisJapCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

// TODO: 2021/9/16 实现redis的插件形式引入
@Configuration
@EnableConfigurationProperties({CacheProperties.class})
@Slf4j
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
    @ConditionalOnMissingBean
    public JapCache japCache(CacheProperties cacheProperties,
                             RedisTemplate<String, Serializable> redisTemplate){
        CacheType type = cacheProperties.getToken().getType();
        if (type.equals(CacheType.DEFAULT))
            return new JapLocalCache();

        if (type.equals(CacheType.REDIS))
            return new RedisJapCache(redisTemplate,cacheProperties);

        log.warn("请自定义实现token缓存，即JapCache接口，并注入bean容器");
        return null;
    }


    // TODO: 2021/9/8 暂时不考虑实现该接口。
    //  原因：AbstractJapStrategy的构造器之一：
    //  public AbstractJapStrategy(JapUserService japUserService, JapConfig japConfig, JapUserStore japUserStore, JapCache japCache)
    //  并没有被四种strategy类的任何一个直接调用。
    //  若要”强行“，则需要用到反射
    @Bean
    @ConditionalOnMissingBean
    public JapUserStore japUserStore(CacheProperties cacheProperties,
                                     JapBasicProperties basicProperties,
                                     RedisTemplate<String,JapUser> redisTemplate) {
        return null;
    }
}
