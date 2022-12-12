package com.example.mine.controller;

import com.example.mine.entity.OAuthToken;
import com.example.mine.service.FeignClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "gateway.json")
    public String testGateway() {
        return "Gateway config success!";
    }
}
