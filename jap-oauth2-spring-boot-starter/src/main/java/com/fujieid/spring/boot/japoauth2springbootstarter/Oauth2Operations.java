package com.fujieid.spring.boot.japoauth2springbootstarter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.oauth2.OAuthConfig;
import com.fujieid.jap.oauth2.Oauth2GrantType;
import com.fujieid.jap.oauth2.Oauth2ResponseType;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oauth2.token.AccessToken;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure.Oauth2Properties;

import java.util.Optional;

public class Oauth2Operations {
    private Oauth2Strategy oauth2Strategy;
    private Oauth2Properties oauth2Properties;

    public Oauth2Operations(Oauth2Strategy oauth2Strategy, Oauth2Properties oauth2Properties) {
        this.oauth2Strategy = oauth2Strategy;
        this.oauth2Properties = oauth2Properties;
    }

    /**
     * authorization_code授权方式，以下是必不可少的配置：<br/>
     * response-type=code<br/>
     * grant-type=authorization_code<br/>
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByAuthorizationCode(String platform){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getResponseType() == Oauth2ResponseType.code && oAuthConfig.getPlatform().equals(platform))
                .findFirst();
        if (first.isEmpty())
            throw new JapException("尚未配置 authorization_code 授权方式");
        OAuthConfig oAuthConfig = first.get();
        return JapUtil.authenticate(this.oauth2Strategy, oAuthConfig);
    }

    /**
     * implicit<br/>
     * response-type=token
     * grant-type 可以不写
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByImplicit(String platform){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getResponseType() == Oauth2ResponseType.token && oAuthConfig.getPlatform().equals(platform))
                .findFirst();
        if (first.isEmpty())
            throw new JapException("尚未配置 implicit 授权方式");
        OAuthConfig oAuthConfig = first.get();
        return JapUtil.authenticate(this.oauth2Strategy, oAuthConfig);
    }

    /**
     * password授权方式,br/>
     * response-type=none<br/>
     * grant-type=password<br/>
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByPassword(String platform, String username, String password){
        OAuthConfig oauthConfig = findOauthConfig(platform, Oauth2GrantType.password);
        OAuthConfig newOauthConfig = BeanUtil.copyProperties(oauthConfig, OAuthConfig.class, "username", "password");
        oauthConfig.setUsername(username);
        oauthConfig.setPassword(password);
        return JapUtil.authenticate(this.oauth2Strategy, newOauthConfig);
    }

    /**
     * clientCredentials授权方式<br/>
     * response-type=none<br/>
     * grant-type=client-credentials<br/>
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByClientCredentials(String platform){
        OAuthConfig oauthConfig = findOauthConfig(platform, Oauth2GrantType.client_credentials);
        return JapUtil.authenticate(this.oauth2Strategy, oauthConfig);
    }


    /**
     * refresh-token<br/>
     * response-type=none<br/>
     * grant-type=refresh-token<br/>
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse refreshToken(String platform, String refreshToken){
        OAuthConfig oauthConfig = findOauthConfig(platform, Oauth2GrantType.refresh_token);
        return this.oauth2Strategy.refreshToken(oauthConfig, refreshToken);
    }

    public JapResponse revokeToken(String platform, String accessToken){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getPlatform().equals(platform) && !ObjectUtil.isNull(oAuthConfig.getRevokeTokenUrl()))
                .findFirst();
        if (first.isEmpty())
            throw new JapException(platform+"尚未配置 revoke-token-url");
        OAuthConfig oAuthConfig = first.get();
        return oauth2Strategy.revokeToken(oAuthConfig,accessToken);

    }

    public JapResponse getUserInfo(String platform, AccessToken accessToken){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getPlatform().equals(platform) && !ObjectUtil.isNull(oAuthConfig.getUserinfoUrl()))
                .findFirst();
        if (first.isEmpty())
            throw new JapException(platform+"尚未配置 user-info-url");
        OAuthConfig oAuthConfig = first.get();
        return oauth2Strategy.getUserInfo(oAuthConfig, accessToken);
    }



    private OAuthConfig findOauthConfig(String platform, Oauth2GrantType grantType){
        Optional<OAuthConfig> first = oauth2Properties.getOauth2()
                .stream()
                .filter(oAuthConfig -> oAuthConfig.getGrantType() == grantType && oAuthConfig.getPlatform().equals(platform))
                .findFirst();
        if (first.isEmpty())
            throw new JapException("尚未配置 authorization_code 授权方式");
        return first.get();
    }

}
