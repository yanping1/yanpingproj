package com.dkha.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ApiVO
 * @Package com.dkha.common.entity.vo
 * @author: panhui
 * @date: 2019/11/27 9:36
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "通用接口参数类 发送和接收通用", description = "用于和底层传输统一参数格式")
public class ApiVO {
    @ApiModelProperty(value = "错误码")
    private Integer code;
    @ApiModelProperty(value = "错误提示")
    private String message;
    @ApiModelProperty(value = "操作命令对应不同的接口")
    private String cmd;
    @ApiModelProperty(value = "请求参数")
    private Object data;

    public ApiVO(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
