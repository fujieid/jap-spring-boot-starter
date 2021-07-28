package com.fujieid.jap.spring.boot.starter.controller;

import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.starter.JapStrategyFactory;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;
import com.fujieid.jap.spring.boot.starter.service.SimpleUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/simple")
public class SimpleController {
    @Autowired
    JapProperties japProperties;
    @Autowired
    SimpleStrategy simpleStrategy;
    @Autowired
    JapStrategyFactory japStrategyFactory;
    @Autowired
    SimpleUserServiceImpl simpleUserService;

    @RequestMapping(method = RequestMethod.GET, path = "/1")
    public JapResponse simple(HttpServletRequest request, HttpServletResponse response) {
        return simpleStrategy.authenticate(japProperties.getSimple(), request, response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/2")
    public JapResponse simple1(HttpServletRequest request, HttpServletResponse response) {
        return japStrategyFactory.authenticate(Strategy.SIMPLE,simpleUserService);
    }

}
