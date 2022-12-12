package com.example.mine.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class JwtObject {

    private static final String CLAIM_KEY_USER_ID = "uid";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_token.expiration}")
    private Long access_token_expiration;

    @Value("${jwt.refresh_token.expiration}")
    private Long refresh_token_expiration;

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private Claims claims = null;

    public void getClaimsFromToken(String token) {
        claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        log.info(claims.toString());
    }

    public Long getUserId() {
        Long userId;
        try {
            userId = (Long) claims.get(CLAIM_KEY_USER_ID);
        } catch (Exception e) {
            userId = 0L;
        }
        return userId;
    }

    public Date getCreatedDate() {
        Date created;
        try {
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDate() {
        Date expiration;
        try {
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public Boolean isTokenExpired() {
        final Date expiration = getExpirationDate();
        return expiration.before(new Date());
    }

    // expiration 单位：秒
    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateAccessToken(Long uid) {
        Map<String, Object> claims = generateClaims(uid);
        return generateAccessToken(claims);
    }

    private Map<String, Object> generateClaims(Long uid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, uid);
        return claims;
    }

    private String generateAccessToken(Map<String, Object> claims) {
        return generateToken(claims, access_token_expiration);
    }

    public String generateRefreshToken(Long uid) {
        Map<String, Object> claims = generateClaims(uid);
        return generateRefreshToken(claims);
    }

    private String generateRefreshToken(Map<String, Object> claims) {
        return generateToken(claims, refresh_token_expiration);
    }

    public Boolean canTokenBeRefreshed(Date lastPasswordReset) {
        final Date created = getCreatedDate();
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired());
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            refreshedToken = generateAccessToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    // 自定义的Claims只包含一个uid，即user_id
    private String generateToken(Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }
}
