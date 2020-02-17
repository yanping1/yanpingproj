package com.dkha.common.enums;

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
public enum ApiQueryEnum {


    LIB_CREATE("创建特征库","CmdLibCreate"),
    LIB_DELETE("删除特征库","CmdLibDelete"),
    FACE_FEATURE_ADD("人脸特征添加","CmdLibPush"),
    FACE_FEATURE_DELETE("人脸特征删除","CmdLibPop"),
    FACE_LIBARY_SEARCH("人脸库检索","CmdFSearch"),
    FACE_SEARCH("人脸检测","CmdFSearchFace"),
    FACE_GROUP_SEARCH("人脸分组检索","CmdFaceGroup"),
    CMDFACE_GROUPONETOONE("1:1","CmdFGroupOneToOne"),
    TASK_ADD("任务添加","CmdWTaskAdd"),
    TASK_DELETE("任务删除","CmdWTaskDel");

    private String message;
    private String value;

    ApiQueryEnum(String message, String value)
    {
        this.message=message;
        this.value=value;
    }


}
