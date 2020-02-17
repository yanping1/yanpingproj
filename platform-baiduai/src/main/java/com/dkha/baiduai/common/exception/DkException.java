package com.dkha.baiduai.common.exception;


import com.dkha.baiduai.common.constant.ErrorEnum;

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
        this.code = ErrorEnum.UNKNOWN_ERROR.getCode();
        this.msg = ErrorEnum.UNKNOWN_ERROR.getMsg();
    }

    public DkException(int code) {
        this.code = code;
        this.msg = ErrorEnum.UNKNOWN_ERROR.getMsg();
    }

    public DkException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DkException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.msg = ErrorEnum.UNKNOWN_ERROR.getMsg();
    }

    public DkException(String msg) {
        super(msg);
        this.code = ErrorEnum.UNKNOWN_ERROR.getCode();
        this.msg = msg;
    }

    public DkException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorEnum.UNKNOWN_ERROR.getCode();
        this.msg = msg;
    }

    public DkException(Throwable e) {
        super(e);
        this.code = ErrorEnum.UNKNOWN_ERROR.getCode();
        this.msg = ErrorEnum.UNKNOWN_ERROR.getMsg();
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
