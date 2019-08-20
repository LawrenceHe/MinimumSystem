package com.example.common.exception;

import lombok.Getter;

@Getter
public enum SystemErrorType implements ErrorType {

    SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE("010404", "服务未找到"),
    GATEWAY_ERROR("010500", "网关异常"),
    REQUEST_METHOD_UNSUPPORTED("010501", "不支持的请求方法"),
    GATEWAY_CONNECT_TIME_OUT("010002", "网关超时"),

    ARGUMENT_NOT_VALID("020000", "请求参数校验不通过"),
    UPLOAD_FILE_SIZE_LIMIT("020001", "上传文件大小超过限制"),

    SQL_DUPLICATE_KEY("030000", "该用户已经存在"),

    USER_NOT_EXIST("030001", "用户不存在"),

    USER_PASSWORD_WRONG("030002", "密码不正确"),

    ILLEGAL_TOKEN("040001", "Token不合法"),

    ILLEGAL_HEADER("040002", "Header不合法"),

    TOKEN_EXPIRED("040003", "Token过期"),

    MESSAGE_TOKEN_ERROR("040004", "短信验证码不正确或者已经过期"),


    SYSTEM_ERROR("-1", "系统异常");

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    SystemErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
