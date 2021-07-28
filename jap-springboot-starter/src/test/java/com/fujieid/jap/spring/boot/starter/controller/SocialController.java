package com.fujieid.jap.spring.boot.starter.controller;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.JapStrategyFactory;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;
import com.fujieid.jap.spring.boot.starter.service.SocialUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/social")
public class SocialController {
    @Autowired
    JapStrategyFactory japStrategyFactory;
    @Autowired
    JapProperties japProperties;
    @Autowired
    SocialStrategy socialStrategy;
    @Autowired
    SocialUserServiceImpl socialUserService;


    @RequestMapping(method = RequestMethod.GET, path = "/1")
    public JapResponse social1(HttpServletRequest request, HttpServletResponse response) {
        return japStrategyFactory.authenticate(Strategy.SOCIAL,socialUserService);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/2")
    public JapResponse social2() {
        return japStrategyFactory.authenticate(Strategy.SOCIAL);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/3")
    public JapResponse social3(HttpServletRequest request, HttpServletResponse response) {
        return socialStrategy.authenticate(japProperties.getSocial(), request, response);
    }
}
