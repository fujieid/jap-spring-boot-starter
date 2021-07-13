package com.fujieid.jap.spring.boot.japsimplespringbootstarter;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.social.SocialConfig;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.Strategy;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {
    @Autowired
    JapStrategyFactory japStrategyFactory;
    @Autowired
    JapUserService userService;
    @Autowired
    JapProperties japProperties;

    @RequestMapping(method = RequestMethod.GET, path = "/simple")
    public JapResponse simple(HttpServletRequest request, HttpServletResponse response) {
        JapResponse simple = japStrategyFactory.authenticate(Strategy.SIMPLE, userService, request, response);
        return simple;
    }
    @RequestMapping(method = RequestMethod.GET, path = "/social")
    public JapResponse social(HttpServletRequest request, HttpServletResponse response) {
        JapResponse social = japStrategyFactory.authenticate(Strategy.SOCIAL, userService, request, response);
        return social;
    }

    @RequestMapping(method = RequestMethod.GET, path = "social1")
    public JapResponse social2(HttpServletRequest request, HttpServletResponse response) {
        SocialStrategy socialStrategy = japStrategyFactory.create(SocialStrategy.class, userService);
        return socialStrategy.authenticate(japProperties.getSocial(), request, response);
    }
}
