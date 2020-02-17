package com.dkha.common.enums;

import lombok.Getter;

/**
 * @version V1.0
 * @Description: TODO(任务状态)
 * All rights 成都电科慧安
 * @Title: TaskStatuEnum
 * @Package com.dkha.common.enums
 * @author: panhui
 * @date: 2019/12/15 16:46
 * @Copyright: 成都电科慧安
 */
@Getter
public enum TaskStatuEnum {
    STATUS_UNDEF("STATUSUNDEF","未知"),
    STATUS_STARTING("STATUSSTARTING","正在打开"),
    STATUS_WORKING("STATUSWORKING","工作中"),
    STATUS_RESTATING("STATUSRESTATING","正在重新打开"),
    STATUS_OVER("STATUSOVER","任务已经结束");
    private String code;
    private String message;

    TaskStatuEnum(String code, String message)
    {
        this.code=code;
        this.message=message;
    }

}
