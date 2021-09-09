package com.fujieid.jap.spring.boot.common.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

/**
 * 缓存配置类
 */
@ConfigurationProperties(prefix = "jap.cache")
@Getter
@Setter
public class CacheProperties {

    /**
     * token缓存配置
     */
    @NestedConfigurationProperty
    private TokenCache token = new TokenCache();
    /**
     * 用户登录信息的缓存配置
     */
    // TODO: 2021/9/8 取个什么名字好呢？表示这是用户登录状态的缓存配置
    @NestedConfigurationProperty
    private UserStore user = new UserStore();


    /**
     * token缓存配置（JapCache接口）
     */
    @Getter
    @Setter
    public static class TokenCache{
        /**
         * token缓存类型
         */
        private CacheType type = CacheType.DEFAULT;
        /**
         * token缓存前缀
         */
        private String prefix = "JAP::TOKEN::CACHE::";
        /**
         * token过期时间
         */
        private Duration expireTime = Duration.ofMinutes(3);
    }

    /**
     * 用户登录信息的缓存配置（JapUserStore接口）
     */
    @Getter
    @Setter
    public static class UserStore{
        /**
         * 用户登录信息的缓存类型，缓存JapUser实例
         */
        private CacheType type = CacheType.DEFAULT;

        private String prefix = "JAP::USER::STORE::";
        private Duration expireTime = Duration.ofMinutes(3);
    }

}
