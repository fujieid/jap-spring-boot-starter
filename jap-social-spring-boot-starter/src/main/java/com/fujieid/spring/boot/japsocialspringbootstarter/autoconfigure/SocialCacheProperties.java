package com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure;

import com.fujieid.jap.spring.boot.common.autoconfigure.CacheType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * AuthStateCache配置类
 */
@ConfigurationProperties(prefix = "jap.social.cache")
@Getter
@Setter
public class SocialCacheProperties {
    private CacheType type = CacheType.DEFAULT;
    private String prefix = "JAP::AUTHSTATE::";
    /**
     * 超时时长，只对redis缓存生效，默认3分钟
     */
    private Duration timeout = Duration.ofMinutes(3);

}
