package com.example.mine.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "手机号登录/注册接口的输入参数")
public class GestureLoginReq {

    @NotBlank
    @Pattern(regexp="^1\\d{10}")
    @ApiModelProperty(value = "手机号码", required = true)
    private String mobile;

    @NotBlank
    @ApiModelProperty(value = "手势密码明文", required = true)
    private String gesture;
}
