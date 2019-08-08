package com.example.demo.controller;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/UserCenter")
@Slf4j
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.POST
            , value = "getUserByName.json"
            , consumes = "application/json"
            , produces = "application/json")
    public User login(@Valid @RequestParam String username) {
        User user = userMapper.getByUsername(username);
        String verifyCode = stringRedisTemplate.opsForValue().get(username);
        // 短信验证码登录，若用户不存在，则自动注册并登录
        user.setUsername(username);
        String password = passwordEncoder.encode(verifyCode);
        user.setPassword(password);

        return user;
    }
}
