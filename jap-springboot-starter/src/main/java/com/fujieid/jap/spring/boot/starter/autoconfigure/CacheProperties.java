package com.fujieid.jap.spring.boot.starter.autoconfigure;

import cn.hutool.core.util.ClassUtil;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapException;

import java.time.Duration;

/**
 * 缓存配置类
 */
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


    public CacheType getType() {
        return type;
    }

    public void setType(CacheType type) {
        this.type = type;
    }

    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Class<?> getCustomClass() {
        return customClass;
    }

    public void setCustomClass(Class<?> customClass) {
        if (!ClassUtil.isAssignable(JapCache.class,customClass)){
            throw new JapException(customClass.getName()+"应为接口JapCache的实现类");
        }
        this.customClass = customClass;
    }

    public String getAuthStatePrefix() {
        return authStatePrefix;
    }

    public void setAuthStatePrefix(String authStatePrefix) {
        this.authStatePrefix = authStatePrefix;
    }

    public String getUserStorePrefix() {
        return userStorePrefix;
    }

    public void setUserStorePrefix(String userStorePrefix) {
        this.userStorePrefix = userStorePrefix;
    }
}
