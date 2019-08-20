package com.example.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@ApiModel
@Slf4j
public class CommonRequestHeader {

    @NotNull(message = "token不允许为null")
    @ApiModelProperty(value = "用户登录态")
    private String token;

    @NotBlank(message = "version不允许为空")
    @ApiModelProperty(value = "APP版本号")
    private String version;

    @NotBlank(message = "平台Id不允许为空")
    @ApiModelProperty(value = "平台Id" +
            "a:android\n" +
            "i:ios\n" +
            "w:小程序\n" +
            "h:H5内页面\n" +
            "这些值可以组合，例如ah指Android的H5内页面")
    private String platformId;

    @NotBlank(message = "设备Id不允许为空")
    @ApiModelProperty(value = "用户设备Id")
    private String deviceId;

    @NotBlank(message = "语言不允许为空")
    @ApiModelProperty(value = "语言, cn: 中文, en: 英文")
    private String lang;

    @ApiModelProperty(value = "扩展字段")
    private Map<String, Object> ext;
}
