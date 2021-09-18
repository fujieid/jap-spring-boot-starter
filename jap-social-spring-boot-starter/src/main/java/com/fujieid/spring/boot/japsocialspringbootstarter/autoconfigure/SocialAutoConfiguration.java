package com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure;

import cn.hutool.core.text.StrFormatter;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.core.exception.JapSocialException;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.common.JapUserServiceType;
import com.fujieid.jap.spring.boot.common.autoconfigure.*;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties({SocialProperties.class, SocialCacheProperties.class})
@AutoConfigureAfter({JapBasicAutoConfiguration.class})
@Slf4j
public class SocialAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SocialStrategy socialStrategy(ApplicationContext applicationContext,
                                         JapBasicProperties basicProperties,
                                         SocialProperties socialProperties,
                                         AuthStateCache authStateCache,
                                         JapCache japCache){
        try {
            JapUserService userService = JapUtil.getUserService(applicationContext, JapUserServiceType.SOCIAL, socialProperties.getSocialUserService());
            return new SocialStrategy(userService, basicProperties.getBasic(), japCache, authStateCache);
        } catch (BeansException | IllegalArgumentException e){
            String error = StrFormatter.format(JapUtil.STRATEGY_NO_USERSERVICE,"SocialStrategy");
            log.error(error);
            throw new JapSocialException(error, e.getCause());
        }
    }
}
