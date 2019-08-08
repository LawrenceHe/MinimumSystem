package com.example.demo.exception;

import lombok.Getter;

@Getter
public enum SystemErrorType implements ErrorType {

    SYSTEM_BUSY(0x00000001, "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE(0x00010404, "服务未找到"),
    GATEWAY_ERROR(0x00010500, "网关异常"),
    GATEWAY_CONNECT_TIME_OUT(0x00010002, "网关超时"),

    ARGUMENT_NOT_VALID(0x00020000, "请求参数校验不通过"),
    UPLOAD_FILE_SIZE_LIMIT(0x00020001, "上传文件大小超过限制"),

    SQL_DUPLICATE_KEY(0x00030000, "该用户已经存在"),

    USER_NOT_EXIST(0x00030001, "用户不存在"),

    USER_PASSWORD_WRONG(0x00030002, "密码不正确"),

    ILLEGAL_TOKEN(0x00040001, "Token不合法"),

    ILLEGAL_HEADER(0x00040002, "Header不合法"),

    TOKEN_EXPIRED(0x00040003, "Token过期"),

    MESSAGE_TOKEN_ERROR(0x00040004, "短信验证码不正确或者已经过期"),

    SYSTEM_ERROR(0xFFFFFFFF, "系统异常");

    /**
     * 错误类型码
     */
    private Integer code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    SystemErrorType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
