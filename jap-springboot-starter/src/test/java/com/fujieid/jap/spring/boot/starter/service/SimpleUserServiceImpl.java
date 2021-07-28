package com.fujieid.jap.spring.boot.starter.service;

import cn.hutool.core.lang.UUID;
import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.JapUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleUserServiceImpl implements JapUserService {
    /**
     * 模拟 DB 操作
     */
    private static final List<JapUser> userDatas = new ArrayList<>();

    static {
        // 模拟数据库中的数据
        userDatas.add(new JapUser().setUsername("jap").setPassword("jap").setUserId("jap"));
        for (int i = 0; i < 10; i++) {
            userDatas.add(new JapUser().setUsername("jap" + i).setPassword("jap" + i).setUserId(UUID.fastUUID().toString()));
        }
    }

    @Override
    public JapUser getByName(String username) {
        return userDatas.stream()
                .filter((user) -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean validPassword(String password, JapUser user) {
        return user.getPassword().equals(password);
    }
}
