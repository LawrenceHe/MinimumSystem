package com.example.demo.controller;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.*;
import com.example.demo.exception.SystemErrorType;
import com.example.demo.provider.AuthProvider;
import com.example.demo.provider.UserCenterProvider;
import com.example.demo.service.JwtBeanService;
import com.example.demo.util.EmailTool;
import com.example.demo.util.JwtObject;
import com.example.demo.util.SHA1;
import com.example.demo.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Random;

@RestController
@RequestMapping("/sign")
@Slf4j
public class CustomController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCenterProvider userCenterProvider;

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private JwtBeanService jwtBeanService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(method = RequestMethod.POST
            , value = "registerByUsername.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result register(@Valid @RequestBody CommonRequest<RegisterReq> req) {
        RegisterReq regInfo = req.getData();
        User newUser = new User();

        newUser.setId(new SnowFlake(3, 3).nextId());
        newUser.setUsername(regInfo.getUsername());
        String pwd = SHA1.encode(regInfo.getPassword());
        newUser.setPassword(pwd);
        newUser.setEnabled(true);

        try {
            userMapper.insertUser(newUser);
        } catch (DuplicateKeyException e) {
            return Result.fail(SystemErrorType.SQL_DUPLICATE_KEY);
        }

        return Result.success();
    }

    @RequestMapping(method = RequestMethod.POST
            , value = "info.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result info(@Valid @RequestBody CommonRequest<Object> req) {

        JwtObject jwtObject = jwtBeanService.getJwtObject();
        User user = userMapper.getUserById(jwtObject.getUserId());
        if (user == null) {
            return Result.fail(SystemErrorType.USER_NOT_EXIST);
        }

        return Result.success(user);
    }

    @RequestMapping(method = RequestMethod.POST
            , value = "loginByUsername.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result login(@Valid @RequestBody CommonRequest<UsernameLoginReq> req) {

        UsernameLoginReq loginInfo = req.getData();
        User user;

        if (!loginInfo.getUsername().isEmpty()) {
            user = userMapper.getByUsername(loginInfo.getUsername());
        } else {
            return Result.fail(SystemErrorType.USER_NOT_EXIST);
        }

        if (user == null) {
            return Result.fail(SystemErrorType.USER_NOT_EXIST);
        }

        String pwd = SHA1.encode(loginInfo.getPassword());
        if (!(pwd.equals(user.getPassword()))) {
            return Result.fail(SystemErrorType.USER_PASSWORD_WRONG);
        }

        JwtObject jwtObject = jwtBeanService.getJwtObject();
        String token = jwtObject.generateAccessToken(user.getId());
        LoginResp resp = new LoginResp();
        resp.setToken(token);
        return Result.success(resp);

    }

    @Autowired
    private EmailTool emailTool;
    @RequestMapping(method = RequestMethod.POST
            , value = "getMessageToken.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result getMessageToken(@Valid @RequestBody CommonRequest<MessageTokenReq> req) {
        MessageTokenReq loginInfo = req.getData();
        String verifyCode = String
                .valueOf(new Random().nextInt(899999) + 100000);
        stringRedisTemplate.opsForValue()
                .set(loginInfo.getMobile(), verifyCode, Duration.ofMinutes(1L));
        log.info("VerifyCode:" + verifyCode);
        emailTool.sendSimpleMail(verifyCode);
        return Result.success();
    }

    @RequestMapping(method = RequestMethod.POST
            , value = "loginByMobile.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result loginByMobile(@Valid @RequestBody CommonRequest<MobileLoginReq> req) {

        MobileLoginReq loginInfo = req.getData();
        User user = userCenterProvider.getUserByUsername(loginInfo.getMobile());
        OAuthToken token = authProvider.getOAuthToken(loginInfo.getMobile(),
                loginInfo.getMessageToken(),
                "password",
                "select",
                "client2",
                "123456");
        return Result.success(token);
//        // 短信验证码登录，若用户不存在，则自动注册并登录
//        if (loginInfo.getMessageToken().equals(verifyCode)) {
//
//            if (user != null) {
//                JwtObject jwtObject = jwtBeanService.getJwtObject();
//                String token = jwtObject.generateAccessToken(user.getId());
//                LoginResp resp = new LoginResp();
//                resp.setToken(token);
//                return Result.success(resp);
//            } else {
//                User newUser = new User();
//                newUser.setId(new SnowFlake(3, 3).nextId());
//                newUser.setUsername(loginInfo.getMobile());
//                newUser.setEnabled(true);
//
//                try {
//                    userMapper.insertUser(newUser);
//                } catch (DuplicateKeyException e) {
//                    return Result.fail(SystemErrorType.SQL_DUPLICATE_KEY);
//                }
//
//                JwtObject jwtObject = jwtBeanService.getJwtObject();
//                String token = jwtObject.generateAccessToken(newUser.getId());
//                LoginResp resp = new LoginResp();
//                resp.setToken(token);
//                return Result.success(resp);
//            }
//        } else {
//            return Result.fail(MESSAGE_TOKEN_ERROR);
//        }

    }
}
