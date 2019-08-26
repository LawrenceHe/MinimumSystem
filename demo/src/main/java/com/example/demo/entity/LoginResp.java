package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginResp {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "accessToken")
    private String accessToken;

    @ApiModelProperty(value = "refreshToken")
    private String refreshToken;

}
