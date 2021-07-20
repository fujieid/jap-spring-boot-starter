package com.fujieid.jap.spring.boot.starter.controller;

import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.simple.SimpleConfig;
import com.fujieid.jap.simple.SimpleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SimpleController {
    @Autowired
    SimpleStrategy simpleStrategy;

    @RequestMapping(method = RequestMethod.GET, path = "/simple")
    public JapResponse simple(HttpServletRequest request, HttpServletResponse response) {
        return simpleStrategy.authenticate(new SimpleConfig(), request, response);
    }

}
