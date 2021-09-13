package com.fujieid.spring.boot.japsocialspringbootstarter;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.social.SocialConfig;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.spring.boot.japsocialspringbootstarter.autoconfigure.SocialProperties;
import me.zhyd.oauth.model.AuthToken;

public class SocialOperations {
    private SocialStrategy socialStrategy;
    private SocialProperties socialProperties;

    public SocialOperations(SocialStrategy socialStrategy, SocialProperties socialProperties){
        this.socialStrategy = socialStrategy;
        this.socialProperties = socialProperties;
    }

    public JapResponse authenticate(String platform){
        SocialConfig socialConfig = this.socialProperties.getSocial().get(platform);
        if (ObjectUtil.isNull(socialConfig))
            throw new JapException("没有"+platform+"相应的配置");
        return JapUtil.authenticate(this.socialStrategy,socialConfig);
    }

    public JapResponse refreshToken(String platform, AuthToken authToken){
        SocialConfig socialConfig = this.socialProperties.getSocial().get(platform);
        if (ObjectUtil.isNull(socialConfig))
            throw new JapException("没有"+platform+"相应的配置");
        return this.socialStrategy.refreshToken(socialConfig, authToken);
    }

    public JapResponse getUserInfo(String platform, AuthToken authToken){
        SocialConfig socialConfig = this.socialProperties.getSocial().get(platform);
        if (ObjectUtil.isNull(socialConfig))
            throw new JapException("没有"+platform+"相应的配置");
        return this.socialStrategy.refreshToken(socialConfig, authToken);
    }
}
