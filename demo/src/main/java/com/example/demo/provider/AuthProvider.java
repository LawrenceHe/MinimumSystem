package com.example.demo.provider;

import com.example.demo.entity.OAuthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-server")
public interface AuthProvider {

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
