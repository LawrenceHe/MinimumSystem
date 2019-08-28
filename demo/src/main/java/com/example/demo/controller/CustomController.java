package com.example.demo.controller;

import com.example.common.entity.CommonRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Random;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "获取手机短信验证码", notes = "根据手机号来获取短信验证码，Demo中短信验证码通过邮件来发送。")
    @RequestMapping(method = RequestMethod.POST
            , value = "getMessageToken.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result getMessageToken(@Valid @RequestBody CommonRequest<MessageTokenReq> req) {
        MessageTokenReq loginInfo = req.getData();
        String verifyCode = String
                .valueOf(new Random().nextInt(899999) + 100000);
        log.info("VerifyCode:" + verifyCode);
        String subject = "短信验证码：" + verifyCode;
        String content = "手机号码：" + loginInfo.getMobile() + "，短信验证码：" + verifyCode + "，有效期10分钟。";
        emailTool.sendSimpleMail(subject, content);
        String encodedVerifyCode = passwordEncoder.encode(verifyCode);
        stringRedisTemplate.opsForValue()
                .set(loginInfo.getMobile(), encodedVerifyCode, Duration.ofMinutes(10L));
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
        stringRedisTemplate.delete(loginInfo.getMobile());
        if (passwordEncoder.matches(loginInfo.getMessageToken(), verifyCode)) {
            // 验证码校验通过之后，若用户不存在，先插入用户数据
            if (user == null) {
                // TODO: 这里是远程调用，根本不会抛异常的，所以这个try catch是无效代码！
                try {
                    user = userCenterProvider.insertUser(loginInfo.getMobile(), passwordEncoder.encode("no-fixed-password"));
                } catch (DuplicateKeyException e) {
                    return Result.fail(SystemErrorType.SQL_DUPLICATE_KEY);
                }
            }
            // 生成Auth并返回数据
            return getLoginResponse(user, loginInfo.getMobile());
        } else {
            return Result.fail(SystemErrorType.MESSAGE_TOKEN_ERROR);
        }
    }

    @ApiOperation(value = "设置手势密码")
    @RequestMapping(method = RequestMethod.POST
            , value = "setGesture.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result setGesture(@Valid @RequestBody CommonRequest<GestureLoginReq> req) {
        String mobile = req.getData().getMobile();
        String gesture = passwordEncoder.encode(req.getData().getGesture());
        userCenterProvider.updateUserGesture(mobile, gesture);
        return Result.success();
    }

    @ApiOperation(value = "通过手势密码登录")
    @RequestMapping(method = RequestMethod.POST
            , value = "loginByGesture.json"
            , consumes = "application/json"
            , produces = "application/json")
    public Result loginByGesture(@Valid @RequestBody CommonRequest<GestureLoginReq> req) {

        GestureLoginReq loginInfo = req.getData();
        User user = userCenterProvider.getUserByUsername(loginInfo.getMobile());
        if (user == null) {
            return Result.fail(SystemErrorType.USER_NOT_EXIST);
        }
        if (passwordEncoder.matches(loginInfo.getGesture(), user.getGesture())) {
            // 生成Auth并返回数据
            return getLoginResponse(user, loginInfo.getMobile());
        } else {
            return Result.fail(SystemErrorType.GESTURE_ERROR);
        }
    }

    private Result getLoginResponse(User user, String mobile) {
        OAuthToken auth = authProvider.getOAuthToken(mobile,
                "no-fixed-password",
                "password",
                "select",
                "client2",
                "123456");
        LoginResp resp = new LoginResp();
        resp.setUserId(user.getId().toString());
        resp.setAccessToken(auth.getAccess_token());
        resp.setRefreshToken(auth.getRefresh_token());
        return Result.success(resp);
    }
}
