package com.example.app.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageInfoResp {

    @ApiModelProperty(value = "页面的JSON数据")
    private String pageInfo;
}
