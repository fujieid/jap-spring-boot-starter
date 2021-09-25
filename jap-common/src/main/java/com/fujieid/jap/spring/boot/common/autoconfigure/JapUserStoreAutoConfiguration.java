package com.fujieid.jap.spring.boot.common.autoconfigure;

import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.core.store.SessionJapUserStore;
import com.fujieid.jap.spring.boot.common.cache.RedisJapUserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

// TODO: 2021/9/8 暂时用不上多数据源的JapUserStore。
//  原因：AbstractJapStrategy的构造器之一：
//  public AbstractJapStrategy(JapUserService japUserService, JapConfig japConfig, JapUserStore japUserStore, JapCache japCache)
//  并没有被四种strategy类的任何一个直接调用。
//  若要”强行“，则需要用到反射。这里只作为一个预留。
@Slf4j
public class JapUserStoreAutoConfiguration {

    /**
     * redis jap user
     */
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnProperty(name = "jap.cache.user.type", havingValue = "redis")
    @AutoConfigureAfter(JapBasicAutoConfiguration.class)
    static class Redis{

        @Bean
        public RedisTemplate<String, JapUser> japUserRedisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String,JapUser> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(connectionFactory);
            return redisTemplate;
        }

        @Bean
        @ConditionalOnMissingBean
        public JapUserStore japUserStore(CacheProperties cacheProperties,
                                         RedisTemplate<String,JapUser> redisTemplate) {
            log.info("redis JapUserStore");
            return new RedisJapUserStore(redisTemplate, cacheProperties);
        }
    }

    /**
     * default jap user
     */
    @ConditionalOnProperty(name = "jap.cache.user.type", havingValue = "default", matchIfMissing = true)
    @AutoConfigureAfter(JapBasicAutoConfiguration.class)
    static class Default{
        @Bean
        @ConditionalOnMissingBean
        public JapUserStore japUserStore(JapBasicProperties japBasicProperties){
            log.info("默认JapUserStore");
            if (japBasicProperties.getBasic().isSso())
                return null;
            return new SessionJapUserStore();
        }
    }

    /**
     * custom jap user
     */
    @ConditionalOnProperty(name = "jap.cache.user.type", havingValue = "custom")
    @AutoConfigureAfter(JapBasicAutoConfiguration.class)
    static class Custom{
        @Bean
        @ConditionalOnMissingBean
        public JapUserStore japUserStore(){
            log.info("自定义JapUserStore");
            return null;
        }
    }
}
