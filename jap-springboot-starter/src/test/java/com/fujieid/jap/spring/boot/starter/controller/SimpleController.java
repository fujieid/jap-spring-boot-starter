package com.fujieid.jap.spring.boot.starter.controller;

import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.starter.JapStrategyFactory;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
import com.fujieid.jap.spring.boot.starter.autoconfigure.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SimpleController {
    @Autowired
    JapProperties japProperties;
    @Autowired
    SimpleStrategy simpleStrategy;
    @Autowired
    JapStrategyFactory japStrategyFactory;

    @RequestMapping(method = RequestMethod.GET, path = "/simple")
    public JapResponse simple(HttpServletRequest request, HttpServletResponse response) {
        return simpleStrategy.authenticate(japProperties.getSimple(), request, response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/simple1")
    public JapResponse simple1(HttpServletRequest request, HttpServletResponse response) {
        return japStrategyFactory.authenticate(Strategy.SIMPLE);
    }

}
