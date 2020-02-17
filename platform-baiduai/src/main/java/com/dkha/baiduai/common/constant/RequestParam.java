package com.dkha.baiduai.common.constant;

/**
 * @author hechenggang
 * @Date 2019/12/25 17:34
 * api接口可选参数列表
 */
public enum RequestParam {
    IMAGE("图片信息", "image"),
    IMAGE_TYPE("图片类型", "image_type"),
    GROUP_ID("用户组id", "group_id"),
    USER_ID("用户id", "user_id"),
    FACE_TOKEN("人脸图片token", "face_token"),
    ERROR_CODE("错误码", "error_code"),
    ERROR_MSG("错误信息", "error_msg");

    private String code;
    private String value;

    RequestParam(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
