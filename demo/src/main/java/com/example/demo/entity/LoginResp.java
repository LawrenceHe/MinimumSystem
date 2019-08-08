package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginResp {

    @ApiModelProperty(value = "登录成功返回Token")
    private String token;
}
