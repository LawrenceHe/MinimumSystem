package com.example.common.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class CommonResponseHeader {

    @NotNull(message = "token不允许为null")
    @ApiModelProperty(value = "用户登录态")
    private String token;

    @ApiModelProperty(value = "扩展字段")
    private Map<String, Object> ext;
}
