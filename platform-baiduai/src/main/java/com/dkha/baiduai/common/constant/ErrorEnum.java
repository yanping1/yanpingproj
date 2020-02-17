package com.dkha.baiduai.common.constant;

/**
 * @author hechenggang
 * @Date 2019/12/26 9:52
 * api接口错误码返回
 */
public enum ErrorEnum {
    SUCCESS(0, "操作成功"),
    UNKNOWN_ERROR(1, "服务器内部错误"),
    TOKE_FAILED(13, "获取token失败"),
    ERROR(60001, "服务器内部错误");

    private int code;
    private String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
