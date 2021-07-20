package com.fujieid.jap.spring.boot.starter.controller;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.JapStrategyFactory;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SocialController {
    @Autowired
    JapStrategyFactory japStrategyFactory;
    @Resource(name = "social")
    JapUserService socialUserService;
    @Autowired
    JapProperties japProperties;
    @Autowired
    SocialStrategy socialStrategy;


    @RequestMapping(method = RequestMethod.GET, path = "/social1")
    public JapResponse social(HttpServletRequest request, HttpServletResponse response) {
        JapResponse social = japStrategyFactory.authenticate(Strategy.SOCIAL, socialUserService, request, response);
        return social;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/social2")
    public JapResponse social1(HttpServletRequest request, HttpServletResponse response) {
        SocialStrategy socialStrategy = japStrategyFactory.create(SocialStrategy.class, socialUserService);
        return socialStrategy.authenticate(japProperties.getSocial(), request, response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/social3")
    public JapResponse social2(HttpServletRequest request, HttpServletResponse response) {
        return socialStrategy.authenticate(japProperties.getSocial(), request, response);
    }
}
