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
public enum ApiVdTypeEnum {
    VDURLRTSP("rtsp流类型url","VdUrlRtsp"),
    VDURLGB28181("gb28181流类型url","VdUrlGb28181"),
    VDURLVFS("本地视频","VdUrlVfs"),
    VDURLHTTP("http视频文件类型url","VdUrlHttp");


    private String message;
    private String value;

    ApiVdTypeEnum(String message, String value)
    {
        this.message=message;
        this.value=value;
    }


}
