package com.example.app.exception;

import com.example.common.exception.ErrorType;
import lombok.Getter;

@Getter
public enum AppException implements ErrorType {

    PAGE_NOT_EXIST("050001", "页面数据获取错误"),
    PAGE_NOT_CONFIG("050002", "该页面未配置数据"),
    PAGE_JSON_ERROR("050003", "页面JSON数据解析异常"),

    SYSTEM_ERROR("-1", "系统异常");

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    AppException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
