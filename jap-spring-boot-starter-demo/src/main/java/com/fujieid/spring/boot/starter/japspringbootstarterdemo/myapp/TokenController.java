package com.fujieid.spring.boot.starter.japspringbootstarterdemo.myapp;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 伪造token
 */
@RestController
public class TokenController {
    @RequestMapping(method = RequestMethod.POST,path = "/oauth/token")
    public String token(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("access_token",UUID.fastUUID().toString());

        return jsonObject.toJSONString();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/oauth/userInfo")
    public String userInfo(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId","liuhaodong666");
        return jsonObject.toJSONString();
    }
}
