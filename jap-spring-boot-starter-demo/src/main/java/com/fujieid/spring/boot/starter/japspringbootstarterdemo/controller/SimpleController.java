package com.fujieid.spring.boot.starter.japspringbootstarterdemo.controller;

import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import com.fujieid.jap.spring.boot.starter.autoconfigure.JapProperties;
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
    JapTemplate japTemplate;

    @RequestMapping(method = RequestMethod.GET, path = "/1")
    public JapResponse simple(HttpServletRequest request, HttpServletResponse response) {
        return simpleStrategy.authenticate(japProperties.getSimple(), request, response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/2")
    public JapResponse simple1(HttpServletRequest request, HttpServletResponse response) {
        return japTemplate.simple();
    }

}
