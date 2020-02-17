package com.dkha.common.jwt;

/**
 * @Author Spring
 * @Since 2019/9/11 17:36
 * @Description
 */
public enum JwtConstantEnum {

    USER_ID("uerId", "用户ID"),
    UUID("uuid", "uuid-用于单账号单登录");


    public String code;
    public String msg;

    JwtConstantEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
