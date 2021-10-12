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

    final private String PLATFORM_NO_AUTH_METHOD = "{} 平台配置信息在application.properties/yml中不存在，或该平台未配置 {} 授权方式";
    final private String PLATFORM_LACK_PROPERTY = "{} 缺少参数 {}";

    public Oauth2Operations(Oauth2Strategy oauth2Strategy, Oauth2Properties oauth2Properties) {
        this.oauth2Strategy = oauth2Strategy;
        this.oauth2Properties = oauth2Properties;
    }

    /**
     * {@code authorization-code} auth mode, essential configure below:
     *
     * <p>response-type=code
     * <p>grant-type=authorization_code
     *
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByAuthorizationCode(String platform){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getResponseType() == Oauth2ResponseType.code && oAuthConfig.getPlatform().equals(platform))
                .findFirst();
        if (!first.isPresent())
            throw new JapException(StrFormatter.format(PLATFORM_NO_AUTH_METHOD,platform,"authorization_code"));
        OAuthConfig oAuthConfig = first.get();
        return super.authenticate(this.oauth2Strategy, oAuthConfig);
    }

    /**
     * {@code implicit} auth mode. Here is some notes while writing application.properties/yml:
     * <p>response-type=token
     * <p>grant-type: none.
     *
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByImplicit(String platform){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getResponseType() == Oauth2ResponseType.token && oAuthConfig.getPlatform().equals(platform))
                .findFirst();
        if (!first.isPresent())
            throw new JapException(StrFormatter.format(PLATFORM_NO_AUTH_METHOD,platform,"implicit"));
        OAuthConfig oAuthConfig = first.get();
        return super.authenticate(this.oauth2Strategy, oAuthConfig);
    }

    /**
     * password auth mode.Here is some notes while writing application.properties/yml:
     * <p>response-type: none
     * <p>grant-type=password
     * @param platform platform
     * @param username username
     * @param password password
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
     * client-credentials auth mode.Here is some notes while writing application.properties/yml:
     * <p>response-type: none
     * <p>grant-type=client-credentials
     *
     * @param platform platform
     * @return JapResponse
     */
    public JapResponse authenticateByClientCredentials(String platform){
        OAuthConfig oauthConfig = findOauthConfigByGrantType(platform, Oauth2GrantType.client_credentials);
        return super.authenticate(this.oauth2Strategy, oauthConfig);
    }


    /**
     * refresh-token auth mode. Here is some notes while writing application.properties/yml:
     * <p>response-type: none
     * <p>grant-type=refresh-token
     *
     * @param platform platform
     * @param refreshToken refreshToken
     * @return JapResponse
     */
    public JapResponse refreshToken(String platform, String refreshToken){
        OAuthConfig oauthConfig = findOauthConfigByGrantType(platform, Oauth2GrantType.refresh_token);
        return this.oauth2Strategy.refreshToken(oauthConfig, refreshToken);
    }

    /**
     * Node that {@code revoke-token-url} is essential in application.properties/yml.
     *
     * @param platform platform
     * @param accessToken accessToken
     * @return JapResponse
     */
    public JapResponse revokeToken(String platform, String accessToken){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getPlatform().equals(platform) && !ObjectUtil.isNull(oAuthConfig.getRevokeTokenUrl()))
                .findFirst();
        if (!first.isPresent())
            throw new JapException(StrFormatter.format(PLATFORM_LACK_PROPERTY,platform,"revoke-token-url"));
        OAuthConfig oAuthConfig = first.get();
        return oauth2Strategy.revokeToken(oAuthConfig,accessToken);

    }

    /**
     * Node that {@code user-info-url} is essential in application.properties/yml.
     *
     * @param platform platform
     * @param accessToken accessToken
     * @return JapResponse
     */
    public JapResponse getUserInfo(String platform, AccessToken accessToken){
        Optional<OAuthConfig> first = this.oauth2Properties.getOauth2().stream()
                .filter(oAuthConfig -> oAuthConfig.getPlatform().equals(platform) && !ObjectUtil.isNull(oAuthConfig.getUserinfoUrl()))
                .findFirst();
        if (!first.isPresent())
            throw new JapException(StrFormatter.format(PLATFORM_LACK_PROPERTY,platform,"user-info-url"));
        OAuthConfig oAuthConfig = first.get();
        return oauth2Strategy.getUserInfo(oAuthConfig, accessToken);
    }


    /**
     * 通过{@link Oauth2GrantType}来寻找配置信息，返回匹配的第一个{@link OAuthConfig}实例。
     *
     * @param platform platform. eg. gitee,github
     * @param grantType grantType:authorization_code, password, client_credentials, refresh_token
     * @return JapResponse
     */
    private OAuthConfig findOauthConfigByGrantType(String platform, Oauth2GrantType grantType){
        Optional<OAuthConfig> first = oauth2Properties.getOauth2()
                .stream()
                .filter(oAuthConfig -> oAuthConfig.getGrantType() == grantType && oAuthConfig.getPlatform().equals(platform))
                .findFirst();
        if (!first.isPresent())
            throw new JapException(StrFormatter.format(PLATFORM_NO_AUTH_METHOD,platform,grantType.toString()));
        return first.get();
    }

}
