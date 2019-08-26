package com.example.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "获取短信验证码接口的输入参数")
@Data
public class PageInfoReq {

    @NotBlank
    @ApiModelProperty(value = "页面名称", required = true)
    private String pageName;
}
