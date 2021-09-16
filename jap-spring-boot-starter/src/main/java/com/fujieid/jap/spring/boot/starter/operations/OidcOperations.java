package com.fujieid.jap.spring.boot.starter.operations;

import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.oidc.OidcConfig;
import com.fujieid.jap.oidc.OidcStrategy;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.spring.boot.japoidcspringbootstarter.autoconfigure.OidcProperties;

public class OidcOperations extends AbstractJapOperations{
    private OidcStrategy oidcStrategy;
    private OidcProperties oidcProperties;

    public OidcOperations(OidcStrategy oidcStrategy, OidcProperties oidcProperties) {
        this.oidcStrategy = oidcStrategy;
        this.oidcProperties = oidcProperties;
    }

    public JapResponse authenticate(String platform){
        OidcConfig oidcConfig = this.oidcProperties.getOidc().get(platform);
        if (ObjectUtil.isNull(oidcConfig))
            throw new JapException("没有"+platform+"平台的相应配置");
        return super.authenticate(this.oidcStrategy,oidcConfig);
    }

}