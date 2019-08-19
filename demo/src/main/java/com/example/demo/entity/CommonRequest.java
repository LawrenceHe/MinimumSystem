package com.example.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Slf4j
@ApiModel(description = "SOA请求接口的一般性结构，包含固定的请求头和可变的请求体")
public class CommonRequest<T> {

    @NotNull(message = "请求头不允许为空")
    @Valid
    @ApiModelProperty(value = "公共请求头部")
    private CommonRequestHeader header;

    @NotNull(message = "请求体不允许为空")
    @Valid
    @ApiModelProperty(value = "公共请求数据体")
    private T data;
}
