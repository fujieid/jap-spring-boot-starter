package com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.config.AuthenticateConfig;
import com.fujieid.jap.simple.SimpleConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "jap")
@Getter
@Setter
public class SimpleProperties {
    @NestedConfigurationProperty
    private SimpleConfig simple = new SimpleConfig();
    Class<? extends JapUserService> simpleUserService;
}
