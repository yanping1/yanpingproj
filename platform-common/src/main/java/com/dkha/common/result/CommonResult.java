package com.dkha.common.result;


import com.dkha.common.exception.DkException;
import com.dkha.common.result.systemcode.SystemCode;
import com.dkha.common.validate.UtilValidate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.Serializable;

/**
 * @Author Spring
 * @Since 2019/8/15 11:55
 * @Description 通用结果类 每个controller都集成该类，从而实现统返回结果
 *                约束如下：成功的状态码返回0
 *                          错误操作或者特殊状态码 统一返回非0 错误码默认采用60001
 */
@ControllerAdvice
public class CommonResult extends Throwable implements Serializable {

	private static final long serialVersionUID = 1L;

    /** 返回信息码 */
    private Integer code;
    /** 返回的文字信息 */
    private String message;
    /** 返回数据 */
    private Object data;

    /**
     * 用于auto write
     */
    public CommonResult(){

    }

    public CommonResult(Object data, Integer code, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public CommonResult(Integer code, String message) {
        this.code = code;
        this.data = null;
        this.message = message;
    }

    /**
     * 操作成功返回集 code为0
     */
    public CommonResult successResult(Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.code = SystemCode.HANDLER_SUCCESS.code;
        commonResult.data = data;
        commonResult.message = SystemCode.HANDLER_SUCCESS.des;
        return commonResult;
    }

    /**
     * 操作成功返回集 code为0
     * @param message
     * @param data
     */
    public CommonResult successResult(String message, Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.code = SystemCode.HANDLER_SUCCESS.code;
        commonResult.data = data;
        commonResult.message = UtilValidate.isEmpty(message) ? SystemCode.HANDLER_SUCCESS.des : message;
        return commonResult;
    }

    /**
     * 操作失败返回集 默认code 60001
     * @param message
     * @return
     */
    public CommonResult failResult(String message) {
        CommonResult commonResult = new CommonResult();
        commonResult.code = SystemCode.HANDLER_FAILED.code;
        commonResult.data = null;
        commonResult.message = message;
        return commonResult;
    }

    /**
     * 操作失败返回集
     * @param data
     * @param message
     * @param code
     */
    public CommonResult failResult(Integer code, String message, Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.code = UtilValidate.isEmpty(code) ? SystemCode.HANDLER_FAILED.code : code;
        commonResult.data = data;
        commonResult.message = UtilValidate.isEmpty(message) ? SystemCode.HANDLER_FAILED.des : message;
        return commonResult;
    }

    /**
     * 用于Hibernate-validate vo校验
     * @param bindingResult
     */
    public void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DkException(bindingResult.getFieldError().getDefaultMessage());
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

