package com.example.demo.entity;

import lombok.Data;

@Data
public class OAuthToken {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;
}
