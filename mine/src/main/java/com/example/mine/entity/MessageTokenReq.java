package com.example.mine.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(description = "获取短信验证码接口的输入参数")
@Data
public class MessageTokenReq {

    @NotBlank
    @Pattern(regexp="^1\\d{10}")
    @ApiModelProperty(value = "手机号码", required = true)
    private String mobile;
}
