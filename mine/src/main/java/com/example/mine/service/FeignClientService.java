package com.example.mine.service;

import com.example.mine.entity.OAuthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authorization-server")
public interface FeignClientService {

    @PostMapping(value = "/oauth/token")
    OAuthToken getOAuthToken(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("grant_type") String grant_type,
            @RequestParam("scope") String scope,
            @RequestParam("client_id") String client_id,
            @RequestParam("client_secret") String client_secret
        );
}
