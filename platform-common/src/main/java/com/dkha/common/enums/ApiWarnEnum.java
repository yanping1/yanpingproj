package com.dkha.common.enums;

import lombok.Getter;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ApiWarnEnum
 * @Package com.dkha.common.enums
 * @author: panhui
 * @date: 2019/12/10 9:05
 * @Copyright: 成都电科慧安
 */
@Getter
public enum  ApiWarnEnum {

    HIT(1,"命中"),MISS(0,"未命中");

    private Integer code;
    private String message;

    ApiWarnEnum(Integer code, String message)
    {
        this.code=code;
        this.message=message;
    }
}
