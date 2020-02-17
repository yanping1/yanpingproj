package com.dkha.common.enums;

/**
 * @author Spring
 * @date 2018/5/18-11:30
 * @Description: 逻辑判断枚举
 */
public enum YNEnums {

    YES("Y", "是"),
    NO("N", "否");

    public String code;
    public String name;

    YNEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
