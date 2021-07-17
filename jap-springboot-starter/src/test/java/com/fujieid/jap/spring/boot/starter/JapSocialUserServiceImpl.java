package com.fujieid.jap.spring.boot.starter;

import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.JapUserService;
import me.zhyd.oauth.model.AuthUser;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("social")
public class JapSocialUserServiceImpl implements JapUserService {
    /**
     * 模拟 DB 操作
     */
    private static List<JapUser> userDatas = Lists.newArrayList();

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
     * 创建并获取第三方用户，相当于第三方登录成功后，将授权关系保存到数据库（开发者业务系统中 social user -> sys user 的绑定关系）
     *
     * @param userInfo JustAuth 中的 AuthUser
     * @return JapUser
     */
    @Override
    public JapUser createAndGetSocialUser(Object userInfo) {
        AuthUser authUser = (AuthUser) userInfo;
        // 查询绑定关系，确定当前用户是否已经登录过业务系统
        JapUser japUser = this.getByPlatformAndUid(authUser.getSource(), authUser.getUuid());
        if (null == japUser) {
            japUser = createJapUser();
            japUser.setAdditional(authUser);
            userDatas.add(japUser);
        }
        return japUser;
    }

    /**
     * 模拟创建用户
     *
     * @return JapUser
     */
    private JapUser createJapUser() {
        JapUser user = new JapUser();
        user.setUserId("1");
        user.setUsername("jap");
        user.setPassword("jappassword");
        return user;
    }

}
