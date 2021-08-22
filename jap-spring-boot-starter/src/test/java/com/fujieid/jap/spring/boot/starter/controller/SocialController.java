package com.fujieid.jap.spring.boot.starter.controller;

import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.social.SocialStrategy;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.service.SocialUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/social")
public class SocialController {
    @Autowired
    JapTemplate japTemplate;
    @Autowired
    JapProperties japProperties;
    @Autowired
    SocialStrategy socialStrategy;
    @Autowired
    SocialUserServiceImpl socialUserService;


    @RequestMapping(method = RequestMethod.GET, path = "/1")
    public JapResponse social1(HttpServletRequest request, HttpServletResponse response) {
        return japTemplate.social("gitee");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/2")
    public JapResponse social3(HttpServletRequest request, HttpServletResponse response) {
        return socialStrategy.authenticate(japProperties.getSocial().get("gitee"), request, response);
    }
}
