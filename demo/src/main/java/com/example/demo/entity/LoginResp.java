package com.example.demo.entity;

import com.example.common.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginResp {

    @ApiModelProperty(value = "登录成功返回Token")
    private User user;

    @ApiModelProperty(value = "Auth相关信息")
    private OAuthToken auth;

}
