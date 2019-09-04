package com.example.app.service;

import org.springframework.security.jwt.Jwt;

public interface IAuthService {

    /**
     * 是否无效authentication
     *
     * @param authentication
     * @return
     */
    boolean invalidJwtAccessToken(String authentication);

    /**
     * 是否有效authentication
     *
     * @param authentication
     * @return
     */
    boolean validJwtAccessToken(String authentication);

    /**
     * 从认证信息中提取jwt token 对象
     *
     * @param authentication 认证信息  Authorization: bearer header.payload.signature
     * @return Jwt对象
     */
    Jwt getJwt(String authentication);
}
