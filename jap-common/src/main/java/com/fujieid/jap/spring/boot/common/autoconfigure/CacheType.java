package com.fujieid.jap.spring.boot.common.autoconfigure;

public enum CacheType {
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
