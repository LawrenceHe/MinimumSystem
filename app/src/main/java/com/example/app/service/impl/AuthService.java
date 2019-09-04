package com.example.app.service.impl;

import com.example.app.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService implements IAuthService {

    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    /**
     * jwt验签
     */
    private MacSigner verifier;

    @Override
    public boolean invalidJwtAccessToken(String authentication) {
        verifier = Optional.ofNullable(verifier).orElse(new MacSigner(signingKey));
        //是否无效true表示无效
        boolean invalid = Boolean.TRUE;

        try {
            Jwt jwt = getJwt(authentication);
            jwt.verifySignature(verifier);
            invalid = Boolean.FALSE;
        } catch (Exception e) {
            log.warn("user token has expired or signature error ");
        }
        return invalid;
    }

    @Override
    public boolean validJwtAccessToken(String authentication) {
        return !invalidJwtAccessToken(authentication);
    }

    @Override
    public Jwt getJwt(String authentication) {
        return JwtHelper.decode(authentication);
    }
}
