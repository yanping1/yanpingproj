package com.dkha.common.exception;

import com.dkha.common.result.systemcode.SystemCode;

/**
 * @author Spring
 * @date 2018/5/18-11:30
 * @Description: 自定义异常
 */
public class DkException extends RuntimeException {
    /**
     * 异常码
     */
    private int code;
    /**
     * 异常信息
     */
    private String msg;

    public DkException() {
        this.code = SystemCode.HANDLER_FAILED.code;
        this.msg = SystemCode.HANDLER_FAILED.des;
    }

    public DkException(int code) {
        this.code = code;
        this.msg = SystemCode.get(code).des;
    }

    public DkException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.msg = SystemCode.get(code).des;
    }

    public DkException(String msg) {
        super(msg);
        this.code = SystemCode.HANDLER_FAILED.code;
        this.msg = msg;
    }

    public DkException(String msg, Throwable e) {
        super(msg, e);
        this.code = SystemCode.HANDLER_FAILED.code;
        this.msg = msg;
    }

    public DkException(Throwable e) {
        super(e);
        this.code = SystemCode.HANDLER_FAILED.code;
        this.msg = SystemCode.HANDLER_FAILED.des;
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
