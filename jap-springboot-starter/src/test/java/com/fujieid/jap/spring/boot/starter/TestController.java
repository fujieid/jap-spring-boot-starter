package com.fujieid.jap.spring.boot.starter;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {
    @Autowired
    JapStrategyFactory japStrategyFactory;
    @Resource(name = "simple")
    JapUserService simpleUserService;
    @Resource(name = "social")
    JapUserService socialUserService;
    @Autowired
    JapProperties japProperties;

    @RequestMapping(method = RequestMethod.GET, path = "/simple")
    public JapResponse simple(HttpServletRequest request, HttpServletResponse response) {
        JapResponse simple = japStrategyFactory.authenticate(Strategy.SIMPLE, simpleUserService, request, response);
        return simple;
    }
    @RequestMapping(method = RequestMethod.GET, path = "/social")
    public JapResponse social(HttpServletRequest request, HttpServletResponse response) {
        JapResponse social = japStrategyFactory.authenticate(Strategy.SOCIAL, socialUserService, request, response);
        return social;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/social1")
    public JapResponse social1(HttpServletRequest request, HttpServletResponse response) {
        SocialStrategy socialStrategy = japStrategyFactory.create(SocialStrategy.class, socialUserService);
        return socialStrategy.authenticate(japProperties.getSocial(), request, response);
    }
}
