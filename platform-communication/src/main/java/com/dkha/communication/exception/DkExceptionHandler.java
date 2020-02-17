package com.dkha.communication.exception;

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
public class DkExceptionHandler extends CommonResult{

    private static final Logger logger = LoggerFactory.getLogger(DkExceptionHandler.class);

    /**
     * Dk自定义异常全局处理
     * @param ex
     * @return
     */
    @ExceptionHandler(DkException.class)
    public CommonResult handleDkException(DkException ex){
        logger.error(ex.getMessage(), ex);
        return failResult(ex.getMsg());
    }

    /**
     * 权限异常拦截--主要用于拦截token
     * @param ex
     * @return
     */
    @ExceptionHandler(DkAuthorityException.class)
    public CommonResult handleAuthorityException(DkAuthorityException ex){
        logger.error(ex.getMessage(), ex);
        return failResult(SystemCode.TOKEN_ERROR.des);
    }

}
