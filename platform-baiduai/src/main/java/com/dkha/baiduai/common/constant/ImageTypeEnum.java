package com.dkha.baiduai.common.constant;

/**
 * @author hechenggang
 * @Date 2019/12/26 9:28
 */
public enum ImageTypeEnum {
    BASE64("BASE64", "BASE64"),
    URL("图片URL地址", "URL"),
    //调用人脸检测接口时，会为每个人脸图片赋予一个唯一的FACE_TOKEN
    FACE_TOKEN("人脸图片的唯一标识", "FACE_TOKEN");


    private String code;
    private String value;

    ImageTypeEnum(String code, String value) {
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
