package com.example.demo.controller;

import com.example.demo.entity.OAuthToken;
import com.example.demo.service.FeignClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private FeignClientService feignClientService;

    @RequestMapping(method = RequestMethod.POST
            , value = "feignRequest.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Object testFeignRequest() {
        OAuthToken token = feignClientService.getOAuthToken(
                "user_1",
                "123456",
                "password",
                "select",
                "client_2",
                "123456");
        return token;
    }
}
