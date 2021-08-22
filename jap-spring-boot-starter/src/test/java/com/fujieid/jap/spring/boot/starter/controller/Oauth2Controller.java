package com.fujieid.jap.spring.boot.starter.controller;

import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import com.fujieid.jap.spring.boot.starter.JapTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {
    @Autowired
    Oauth2Strategy oauth2Strategy;
    @Autowired
    JapTemplate japTemplate;


    @RequestMapping(method = RequestMethod.GET, path = "/1")
    public JapResponse oauth2(HttpServletRequest request, HttpServletResponse response){
        return japTemplate.oauth("gitee");
    }

}
