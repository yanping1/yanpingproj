package com.dkha.api.modules;

import com.dkha.api.modules.errnums.ErrEnum;
import com.dkha.common.exception.DkException;
import com.dkha.common.validate.UtilValidate;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.io.Serializable;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ReturnVO
 * @Package com.dkha.api.modules
 * @author: panhui
 * @date: 2019/12/2 13:34
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="API通用返回", description="Api通用返回結構類")
public class ReturnVO implements Serializable {

    private Integer code;
    private String message;
    private Object data;
    private static final long serialVersionUID = 1L;


    public ReturnVO(Object data, Integer code, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ReturnVO(Integer code, String message) {
        this.code = code;
        this.data = null;
        this.message = message;
    }

    /**
     * 操作成功返回集 code为200
     */
    public ReturnVO successResult(Object data) {
        ReturnVO ReturnVO = new ReturnVO();
        ReturnVO.code = ErrEnum.OK.getCode();
        ReturnVO.data = data;
        ReturnVO.message = ErrEnum.OK.getMsg();
        return ReturnVO;
    }

    /**
     * 操作成功返回集 code为200
     * @param message
     * @param data
     */
    public ReturnVO successResult(String message, Object data) {
        ReturnVO ReturnVO = new ReturnVO();
        ReturnVO.code = ErrEnum.OK.getCode();
        ReturnVO.data = data;
        ReturnVO.message = UtilValidate.isEmpty(message) ? ErrEnum.OK.getMsg() : message;
        return ReturnVO;
    }

    /**
     * 操作失败返回集 默认code 500
     * @param message
     * @return
     */
    public ReturnVO failResult(String message) {
        ReturnVO ReturnVO = new ReturnVO();
        ReturnVO.code = ErrEnum.ERROR.getCode();
        ReturnVO.data = null;
        ReturnVO.message = message;
        return ReturnVO;
    }

    /**
     * 参数不正确 默认code
     * @param message
     * @return
     */
    public ReturnVO paramErrorResult(String message) {
        ReturnVO ReturnVO = new ReturnVO();
        ReturnVO.code = ErrEnum.BAD_REQUEST.getCode();
        ReturnVO.data = null;
        ReturnVO.message = (UtilValidate.isEmpty(message)?ErrEnum.BAD_REQUEST.getMsg():message);
        return ReturnVO;
    }

    /**
     * 没有查询到数据 默认code
     * @param message
     * @return
     */
    public ReturnVO notInfoResult(String message) {
        ReturnVO ReturnVO = new ReturnVO();
        ReturnVO.code = ErrEnum.NOT_FOUND.getCode();
        ReturnVO.data = null;
        ReturnVO.message = (UtilValidate.isEmpty(message)?ErrEnum.NOT_FOUND.getMsg():message);
        return ReturnVO;
    }

    /**
     * 操作失败返回集
     * @param
     * @return
     */
    public ReturnVO fail() {
        ReturnVO ReturnVO = new ReturnVO();
        ReturnVO.code = ErrEnum.ERROR.getCode();
        ReturnVO.data = null;
        ReturnVO.message = ErrEnum.ERROR.getMsg();
        return ReturnVO;
    }

    /**
     * 操作失败返回集
     * @param data
     * @param message
     * @param code
     */
    public ReturnVO failResult(Integer code, String message, Object data) {
        ReturnVO ReturnVO = new ReturnVO();
        ReturnVO.code = UtilValidate.isEmpty(code) ? ErrEnum.ERROR.getCode() : code;
        ReturnVO.data = data;
        ReturnVO.message = UtilValidate.isEmpty(message) ? ErrEnum.ERROR.getMsg() : message;
        return ReturnVO;
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
}
