package com.example.mine.interceptor;

import com.example.common.exception.BaseException;
import com.example.common.exception.SystemErrorType;
import com.example.mine.service.JwtBeanService;
import com.example.mine.util.HttpHelper;
import com.example.mine.util.JwtObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CustomInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtBeanService jwtBeanService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String body = HttpHelper.getBodyString(request);
        log.info(body);

        // header不允许为空
        JsonNode headerNode = new ObjectMapper().readTree(body).get("header");
        if (headerNode == null) {
            throw new BaseException(SystemErrorType.ILLEGAL_HEADER);
        }

        // 特定的Path不进行token校验
        String servletPath = request.getRequestURI();
        if (servletPath.contains("/user/loginByUsername.json")
            || servletPath.contains("/user/registerByUsername.json")
            || servletPath.contains("/user/getMessageToken.json")
            || servletPath.contains("/user/loginByMobile.json")) {
            return true;
        }

        // 校验Token
        String token = headerNode.get("token").asText("");
        JwtObject jwtObject = jwtBeanService.getJwtObject();
        try {
            jwtObject.getClaimsFromToken(token);
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            throw new BaseException(SystemErrorType.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException
                | MalformedJwtException
                | SignatureException
                | IllegalArgumentException e) {
            log.info(e.getMessage());
            throw new BaseException(SystemErrorType.ILLEGAL_TOKEN);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
