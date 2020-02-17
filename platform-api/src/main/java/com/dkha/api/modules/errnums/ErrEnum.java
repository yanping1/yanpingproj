package com.dkha.api.modules.errnums;

import lombok.Getter;

/**
 * @version V1.0
 * @Description: TODO(api中返回的错误代码)
 * All rights 成都电科慧安
 * @Title: errEnum
 * @Package com.dkha.api.modules.errnums
 * @author: panhui
 * @date: 2019/12/2 11:42
 * @Copyright: 成都电科慧安
 */
@Getter
public enum  ErrEnum {
    OK(200,"成功"),CREATED(201,"创建成功"),

    ACCEPTED(202,"更新成功"),BAD_REQUEST(400,"请求的地址不存在或者包含不支持的参数"),

    UNAUTHORIZED(401,"未授权"),FORBIDDEN(403,"被禁止访问"),

    NOT_FOUND(404,"请求资源不存在"),ERROR(500,"内部错误");


    private Integer code;
    private String msg;

    ErrEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
