package com.fujieid.jap.spring.boot.common.autoconfigure;

import com.fujieid.jap.core.config.JapConfig;
import com.fujieid.jap.sso.config.JapSsoConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "jap")
@Getter
@Setter
public class JapBasicProperties {

    @NestedConfigurationProperty
    JapConfig basic = new JapConfig();

    @NestedConfigurationProperty
    JapSsoConfig sso = new JapSsoConfig();

    public void setSso(JapSsoConfig sso) {
        // 设置sso的时候要把该sso配置到JapConfig对象中
        this.getBasic().setSsoConfig(sso);
        this.sso = sso;
    }

}
