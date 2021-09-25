package com.fujieid.jap.spring.boot.starter.operations;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.exception.JapException;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.oauth2.OAuthConfig;
import com.fujieid.jap.oauth2.Oauth2GrantType;
import com.fujieid.jap.oauth2.Oauth2ResponseType;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.oauth2.token.AccessToken;
import com.fujieid.spring.boot.japoauth2springbootstarter.autoconfigure.Oauth2Properties;

import java.util.Optional;

public class Oauth2Operations extends AbstractJapOperations{
    private Oauth2Strategy oauth2Strategy;
    private Oauth2Properties oauth2Properties;

    final private String PLATFORM_NO_AUTH_METHOD = "{} 平台尚未配置 {} 授权方式，或配置有误";
    final private String PLATFORM_LACK_PROPERTY = "{} 缺少参数 {}";

    public Oauth2Operations(Oauth2Strategy oauth2Strategy, Oauth2Properties oauth2Properties) {
        this.oauth2Strategy = oauth2Strategy;
        this.oauth2Properties = oauth2Properties;
    }

    /**
     * authorization-code授权方式，以下是必不可少的配置：<br/>
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
            throw new JapException(StrFormatter.format(PLATFORM_NO_AUTH_METHOD,platform,"authorization_code"));
        OAuthConfig oAuthConfig = first.get();
        return super.authenticate(this.oauth2Strategy, oAuthConfig);
    }

    /**
     * implicit授权方式<br/>
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
            throw new JapException(StrFormatter.format(PLATFORM_NO_AUTH_METHOD,platform,"implicit"));
        OAuthConfig oAuthConfig = first.get();
        return super.authenticate(this.oauth2Strategy, oAuthConfig);
    }

    /**
     * password授权方式<br/>
     * response-type=none<br/>
     * grant-type=password<br/>
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByPassword(String platform, String username, String password){
        OAuthConfig oauthConfig = findOauthConfigByGrantType(platform, Oauth2GrantType.password);
        OAuthConfig newOauthConfig = BeanUtil.copyProperties(oauthConfig, OAuthConfig.class, "username", "password");
        newOauthConfig.setUsername(username);
        newOauthConfig.setPassword(password);
        return super.authenticate(this.oauth2Strategy, newOauthConfig);
    }

    /**
     * clientCredentials授权方式<br/>
     * response-type=none<br/>
     * grant-type=client-credentials<br/>
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByClientCredentials(String platform){
        OAuthConfig oauthConfig = findOauthConfigByGrantType(platform, Oauth2GrantType.client_credentials);
        return super.authenticate(this.oauth2Strategy, oauthConfig);
    }


    /**
     * refresh-token授权方式<br/>
     * response-type=none<br/>
     * grant-type=refresh-token<br/>
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse refreshToken(String platform, String refreshToken){
        OAuthConfig oauthConfig = findOauthConfigByGrantType(platform, Oauth2GrantType.refresh_token);
        return this.oauth2Strategy.refreshToken(oauthConfig, refreshToken);
    }

    /**
     * 配置文件中必不可少的是revoke-token-url
     * @param platform platform
     * @param accessToken accessToken
     * @return
     */
    public JapResponse revokeToken(String platform, String accessToken){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getPlatform().equals(platform) && !ObjectUtil.isNull(oAuthConfig.getRevokeTokenUrl()))
                .findFirst();
        if (first.isEmpty())
            throw new JapException(StrFormatter.format(PLATFORM_LACK_PROPERTY,platform,"revoke-token-url"));
        OAuthConfig oAuthConfig = first.get();
        return oauth2Strategy.revokeToken(oAuthConfig,accessToken);

    }

    /**
     * 配置文件中必不可少的是user-info-url
     * @param platform platform
     * @param accessToken accessToken
     * @return
     */
    public JapResponse getUserInfo(String platform, AccessToken accessToken){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getPlatform().equals(platform) && !ObjectUtil.isNull(oAuthConfig.getUserinfoUrl()))
                .findFirst();
        if (first.isEmpty())
            throw new JapException(StrFormatter.format(PLATFORM_LACK_PROPERTY,platform,"user-info-url"));
        OAuthConfig oAuthConfig = first.get();
        return oauth2Strategy.getUserInfo(oAuthConfig, accessToken);
    }


    /**
     * 通过{@link Oauth2GrantType}来寻找配置信息，返回匹配的第一个{@link OAuthConfig}实例
     * @param platform platform. eg. gitee,github
     * @param grantType grantType:authorization_code, password, client_credentials, refresh_token
     * @return
     */
    private OAuthConfig findOauthConfigByGrantType(String platform, Oauth2GrantType grantType){
        Optional<OAuthConfig> first = oauth2Properties.getOauth2()
                .stream()
                .filter(oAuthConfig -> oAuthConfig.getGrantType() == grantType && oAuthConfig.getPlatform().equals(platform))
                .findFirst();
        if (first.isEmpty())
            throw new JapException(StrFormatter.format(PLATFORM_NO_AUTH_METHOD,platform,grantType.toString()));
        return first.get();
    }

}
