package com.fujieid.jap.spring.boot.common.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.config.JapConfig;
import com.fujieid.jap.core.context.JapAuthentication;
import com.fujieid.jap.core.store.JapUserStore;
import com.fujieid.jap.core.util.JapTokenHelper;
import com.fujieid.jap.core.util.JapUtil;
import com.fujieid.jap.spring.boot.common.autoconfigure.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 缓存用户的登录状态的redis实现类。用户登录成功后将用户信息保存在JapUser对象中并缓存
 */
public class RedisJapUserStore implements JapUserStore {
    private RedisTemplate<String,JapUser> redisTemplate;
    private CacheProperties cacheProperties;

    public RedisJapUserStore(RedisTemplate<String,JapUser> redisTemplate, CacheProperties cacheProperties) {
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }

    // TODO: 2021/9/8 思考：下列分为两个步骤：用户登录信息存入缓存；token存入缓存。
    //  如何保证原子性呢？或者说需要保证原子性吗，毕竟这只是缓存，无状态的。
    @Override
    public JapUser save(HttpServletRequest request, HttpServletResponse response, JapUser japUser) {
        HttpSession session = request.getSession();
        JapUser newUser = BeanUtil.copyProperties(japUser, JapUser.class);
        newUser.setPassword(null);
        redisTemplate.opsForValue().set(cacheProperties.getUser().getPrefix()+session.getId(),
                newUser
                ,cacheProperties.getUser().getExpireTime());
        JapConfig config = JapAuthentication.getContext().getConfig();
        if (!config.isSso()){
            String token = JapUtil.createToken(japUser, request);
            JapTokenHelper.saveUserToken(japUser.getUserId(), token);
            japUser.setToken(token);
        }
        return japUser;
    }

    @Override
    public void remove(HttpServletRequest request, HttpServletResponse response) {
        JapConfig config = JapAuthentication.getContext().getConfig();
        if (!config.isSso()){
            JapUser japUser = this.get(request, response);
            if (!ObjectUtil.isNull(japUser)){
                JapTokenHelper.removeUserToken(japUser.getUserId());
            }
        }
        HttpSession session = request.getSession();
        redisTemplate.delete(cacheProperties.getUser().getPrefix()+session.getId());
        session.invalidate();
    }

    @Override
    public JapUser get(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return redisTemplate.opsForValue().get(cacheProperties.getUser().getPrefix()+session.getId());
    }
}
