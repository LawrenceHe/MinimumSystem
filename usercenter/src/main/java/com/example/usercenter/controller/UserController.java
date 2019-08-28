package com.example.usercenter.controller;

import com.example.usercenter.dao.UserMapper;
import com.example.common.entity.Role;
import com.example.common.entity.User;
import com.example.usercenter.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(method = RequestMethod.POST
            , value = "insertUser.json")
    public User insertUser(@Valid @RequestParam String mobile
            , @Valid @RequestParam String password) {
        User user = new User();
        user.setId(new SnowFlake(3, 3).nextId());
        user.setUsername(mobile);
        user.setPassword(password);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        try {
            userMapper.insert(user);
            return user;
        } catch (DuplicateKeyException e) {
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST
            , value = "getUserByMobile.json")
    public User getUserByMobile(@Valid @RequestParam String mobile) {
        return userMapper.getByUsername(mobile);
    }

    @RequestMapping(method = RequestMethod.POST
            , value = "getUserById.json")
    public User getUserById(@Valid @RequestParam Long id) {
        return userMapper.getUserById(id);
    }

    @RequestMapping(method = RequestMethod.POST
            , value = "getUserByUsername.json")
    public User getUserByUsername(@Valid @RequestParam String username) {
        return userMapper.getByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST
            , value = "getRolesById.json")
    public Set<Role> getRolesById(@Valid @RequestParam Long id) {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("admin", "lawrence", "core"));
        return roles;
    }

    @RequestMapping(method = RequestMethod.POST
            , value = "updateUserGesture.json")
    public void updateUserGesture(@Valid @RequestParam String mobile, @Valid @RequestParam String gesture) {
        userMapper.updateUserGesture(mobile, gesture);
    }

}
