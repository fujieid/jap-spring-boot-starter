package com.fujieid.jap.spring.boot.common.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 缓存配置类
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "jap.cache")
public class CacheProperties {
    /**
     * 缓存类型
     */
    // TODO: 2021/8/22 三个缓存接口JapUserStore、JapCache、AuthStateCache，如果引入了redis，
    //  那么它们全部都采用redis作为缓存，还是通过配置文件单独确定各自的缓存类型？
    /**
     * JapCache接口的缓存类型，也是token的缓存类型
     */
    private CacheType tokenCacheType = CacheType.DEFAULT;
    /**
     * JapUserStore接口的缓存类型，也是用户登录状态的缓存类型
     */
    private CacheType userStoreType = CacheType.DEFAULT;


    /**
     * 若缓存类型为custom则需指定实现类
     */
    private Class<?> customClass;

    /**
     * 缓存前缀
     */
    private String cachePrefix = "JAP::CACHE::";

    private String userStorePrefix = "JAP::USERSTORE::";

    /**
     * 超时时长，目前只对redis缓存生效，默认3分钟
     */
    private Duration timeout = Duration.ofMinutes(3);


}
