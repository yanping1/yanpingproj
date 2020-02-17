package com.dkha.api.common.exception;

import com.dkha.api.modules.errnums.ErrEnum;
import com.dkha.common.exception.DkAuthorityException;
import com.dkha.common.exception.DkException;
import com.dkha.common.result.CommonResult;
import com.dkha.common.result.systemcode.SystemCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Spring
 * @Since 2019/11/11 16:42
 * @Description 全局异常处理类--用于非系统异常处理
 * 系统异常全局异常处理类
 */
@RestControllerAdvice
public class DkExceptionHandler extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(DkExceptionHandler.class);

    /**
     * 异常码
     */
    private int code;
    /**
     * 异常信息
     */
    private String msg;

    public DkExceptionHandler() {
        this.code = ErrEnum.ERROR.getCode();
        this.msg = ErrEnum.ERROR.getMsg();
    }


    public DkExceptionHandler(String msg) {
        super(msg);
        this.code = ErrEnum.ERROR.getCode();
        this.msg = msg;
    }

    public DkExceptionHandler(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrEnum.ERROR.getCode();
        this.msg = msg;
    }

    public DkExceptionHandler(Throwable e) {
        super(e);
        this.code = ErrEnum.ERROR.getCode();
        this.msg = ErrEnum.ERROR.getMsg();
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
