package com.fujieid.jap.spring.boot.starter.service;

import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.oauth2.token.AccessToken;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapServiceType;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;
import com.xkcoding.json.JsonUtil;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(JapServiceType.OAUTH2)
public class Oauth2UserServiceImpl implements JapUserService {
    /**
     * 模拟 DB 操作
     */
    private static final List<JapUser> userDatas = Lists.newArrayList();

    /**
     * 根据第三方平台标识（platform）和第三方平台的用户 uid 查询数据库
     *
     * @param platform 第三方平台标识
     * @param uid      第三方平台的用户 uid
     * @return JapUser
     */
    @Override
    public JapUser getByPlatformAndUid(String platform, String uid) {
        // FIXME 注意：此处仅作演示用，并没有判断 platform，实际业务系统中需要根据 platform 和 uid 进行获取唯一用户
        return userDatas.stream().filter(user -> user.getUserId().equals(uid)).findFirst().orElse(null);
    }

    /**
     * 创建并获取第三方用户，相当于第三方登录成功后，将授权关系保存到数据库（开发者业务系统中 oauth2 user -> sys user 的绑定关系）
     *
     * @param platform 第三方平台标识
     * @param userInfo JustAuth 中的 AuthUser
     * @return JapUser
     */
    @Override
    public JapUser createAndGetOauth2User(String platform, Map<String, Object> userInfo, Object tokenInfo) {
        // FIXME 业务端可以对 tokenInfo 进行保存或其他操作
        AccessToken accessToken = (AccessToken) tokenInfo;
        System.out.println(JsonUtil.toJsonString(accessToken));
        // FIXME 注意：此处仅作演示用，不同的 oauth 平台用户id都不一样，此处需要开发者自己分析第三方平台的用户信息，提取出用户的唯一ID
        String uid = (String) userInfo.get("userId");
        // 查询绑定关系，确定当前用户是否已经登录过业务系统
        JapUser japUser = this.getByPlatformAndUid(platform, uid);
        if (null == japUser) {
            japUser = createJapUser();
            japUser.setAdditional(userInfo);
            userDatas.add(japUser);
        }
        return japUser;
    }

    private JapUser createJapUser() {
        JapUser user = new JapUser();
        user.setUserId("1");
        user.setUsername("justauth");
        user.setPassword("justauthpassword");
        return user;
    }

}
