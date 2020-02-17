package com.dkha.baiduai.common.exception;

import com.dkha.baiduai.common.constant.ErrorEnum;
import com.dkha.common.entity.vo.ApiVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Spring
 * @Since 2019/11/11 16:42
 * @Description 全局异常处理类--用于非系统异常处理
 * 系统异常全局异常处理类
 */
@RestControllerAdvice
public class DkExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DkExceptionHandler.class);

    /**
     * Dk自定义异常全局处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DkException.class)
    public ApiVO handleDkException(DkException ex, HttpServletRequest request) {
        logger.error(ex.getMessage(), ex);
        ApiVO apiVO = new ApiVO(ErrorEnum.ERROR.getCode(), ex.getMessage());
        return apiVO;
    }

//    /**
//     * 参数校验异常处理
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public CommonResult handleDkException(MethodArgumentNotValidException ex) {
//        logger.error(ex.getMessage(), ex);
//        return failResult(ex.getBindingResult().getFieldError().getDefaultMessage());
//
//    }
//
//    /**
//     * 权限异常拦截--主要用于拦截token
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(DkAuthorityException.class)
//    public CommonResult handleAuthorityException(DkAuthorityException ex) {
//        logger.error(ex.getMessage(), ex);
//        return failResult(SystemCode.TOKEN_ERROR.des);
//    }

    @ExceptionHandler(Exception.class)
    public ApiVO handleException(Exception ex, HttpServletRequest request) {
        logger.error(ex.getMessage(), ex);
        ApiVO apiVO = new ApiVO(ErrorEnum.ERROR.getCode(), ex.getMessage());
        return apiVO;
    }
}
