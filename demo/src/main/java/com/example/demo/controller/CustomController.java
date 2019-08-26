package com.example.demo.controller;

import com.example.common.entity.Result;
import com.example.common.entity.User;
import com.example.common.exception.SystemErrorType;
import com.example.demo.entity.*;
import com.example.demo.provider.AuthProvider;
import com.example.demo.provider.UserCenterProvider;
import com.example.demo.util.EmailTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import static com.example.common.exception.SystemErrorType.MESSAGE_TOKEN_ERROR;

@RestController
@RequestMapping("/sign")
@Api("/sign")
@Slf4j
public class CustomController {

    @Autowired
    private UserCenterProvider userCenterProvider;

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailTool emailTool;

    @ApiOperation(value = "获取手机短信验证码", notes = "根据手机号来获取短信验证码，Demo中短信验证码通过邮件来发送。")
    @RequestMapping(method = RequestMethod.POST
            , value = "getMessageToken.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result getMessageToken(@Valid @RequestBody CommonRequest<MessageTokenReq> req) {
        MessageTokenReq loginInfo = req.getData();
        String verifyCode = String
                .valueOf(new Random().nextInt(899999) + 100000);
        stringRedisTemplate.opsForValue()
                .set(loginInfo.getMobile(), verifyCode, Duration.ofMinutes(10L));
        log.info("VerifyCode:" + verifyCode);
        String subject = "短信验证码：" + verifyCode;
        String content = "手机号码：" + loginInfo.getMobile() + "，短信验证码：" + verifyCode + "，有效期10分钟。";
        emailTool.sendSimpleMail(subject, content);
        return Result.success();
    }

    @ApiOperation(value = "通过手机号进行登录/注册", notes = "通过手机号进行登录/注册，若用户存在则登录，若用户不存在则自动注册并登录。")
    @RequestMapping(method = RequestMethod.POST
            , value = "loginByMobile.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result loginByMobile(@Valid @RequestBody CommonRequest<MobileLoginReq> req) {

        MobileLoginReq loginInfo = req.getData();
        User user = userCenterProvider.getUserByUsername(loginInfo.getMobile());
        // 短信验证码登录，若用户不存在，则自动注册并登录
        String verifyCode = stringRedisTemplate.opsForValue().get(loginInfo.getMobile());
        if (loginInfo.getMessageToken().equals(verifyCode)) {
            // 验证码校验通过之后，若用户不存在，先插入用户数据
            if (user == null) {
                try {
                    user = userCenterProvider.insertUser(loginInfo.getMobile());
                } catch (DuplicateKeyException e) {
                    return Result.fail(SystemErrorType.SQL_DUPLICATE_KEY);
                }
            }
            // 生成Auth并返回数据
            OAuthToken auth = authProvider.getOAuthToken(loginInfo.getMobile(),
                    loginInfo.getMessageToken(),
                    "password",
                    "select",
                    "client2",
                    "123456");
            LoginResp resp = new LoginResp();
            resp.setUser(user);
            resp.setAuth(auth);
            return Result.success(resp);

        } else {
            return Result.fail(MESSAGE_TOKEN_ERROR);
        }

    }
}
