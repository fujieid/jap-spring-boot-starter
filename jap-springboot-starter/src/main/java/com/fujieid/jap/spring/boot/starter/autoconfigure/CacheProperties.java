package com.fujieid.jap.spring.boot.starter.autoconfigure;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

/**
 * 缓存配置类
 */
@Getter
@Setter
public class CacheProperties {
    /**
     * 缓存类型
     */
    private CacheType type = CacheType.DEFAULT;

    /**
     * 若缓存类型为custom则需指定实现类
     */
    private Class<?> customClass;

    /**
     * 缓存前缀，目前只对redis缓存生效，默认 JAP::CACHE::
     */
    private String cachePrefix = "JAP::CACHE::";

    private String authStatePrefix = "JAP::AUTHSTATE::";

    private String userStorePrefix = "JAP::USERSTORE::";

    /**
     * 超时时长，目前只对redis缓存生效，默认3分钟
     */
    private Duration timeout = Duration.ofMinutes(3);


    public enum CacheType{
        /**
         * 使用JustAuth内置的缓存
         */
        DEFAULT,
        /**
         * 使用Redis缓存
         */
        REDIS,
        /**
         * 自定义缓存，若采用，需实现{@link com.fujieid.jap.core.cache.JapCache}接口并配置
         */
        CUSTOM
    }
}
