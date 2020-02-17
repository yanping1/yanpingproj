package com.dkha.api.sdk.wy.modules.enums;

import lombok.Getter;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: QueryEnum
 * @Package com.dkha.wy.modules.enums
 * @author: panhui
 * @date: 2019/11/19 10:36
 * @Copyright: 成都电科慧安
 */
@Getter
public enum  QueryEnum {

    LIB_SEARCH("查询特征库","CmdLibQuery"),
    LIB_CREATE("创建特征库","CmdLibCreate"),
    LIB_DELETE("删除特征库","CmdLibDelete"),
    PIC_SEARCH("图片搜索查找人脸坐标（库不存在则不会去库中检索）","CmdLibDelete"),
    PIC_GROUP("图片分组","CmdFGroup"),
    FACE_FEATURE_ADD("人脸特征添加","CmdLibPush"),
    FACE_FEATURE_DELETE("人脸特征删除","CmdLibPop"),
    TASK_SEARCH("任务查询","CmdWTaskQuery"),
    TASK_ADD("任务添加","CmdWTaskAdd"),
    TASK_DELETE("任务删除","CmdWTaskDel"),
    TASK_ALARM("任务报警","CmdVdFSAlarm"),
    TASK_STATUS("解码状态变化","CmdVdStatusChg"),
    WAPPID_DISTRIBUTION("wappId分配","CmdDistribAppId"),
    WAPPID_LOGOUT("wappId注销","CmdLogoutAppId");

    private String message;
    private String value;

    QueryEnum(String message,String value)
    {
        this.message=message;
        this.value=value;
    }

}
