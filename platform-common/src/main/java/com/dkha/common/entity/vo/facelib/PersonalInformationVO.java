package com.dkha.common.entity.vo.facelib;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: 人员信息（年龄** 性别 ** 身份证** 特征）
 * All rights 成都电科慧安
 * @Title: PersonalInformationVO
 * @Package com.dkha.common.entity.vo.facelib
 * @author: yangjun
 * @date: 2019/11/27 10:09
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人员信息通用接口参数类", description="用于和底层传输统一参数格式")
public class PersonalInformationVO {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private String age;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "身份证号码")
    private String idCard;
    @ApiModelProperty(value = "特征")
    private String feature;

}