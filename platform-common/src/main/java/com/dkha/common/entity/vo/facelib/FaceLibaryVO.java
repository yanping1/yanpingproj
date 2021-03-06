package com.dkha.common.entity.vo.facelib;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description: 人脸入库请求
 * All rights 成都电科慧安
 * @Title: FaceLibaryVO
 * @Package com.dkha.common.entity.vo.facelib
 * @author: yangjun
 * @date: 2019/11/27 10:03
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人脸入库通用接口请求参数类", description="用于和底层传输统一参数格式")
public class FaceLibaryVO {
    @ApiModelProperty(value = "库id")
    private String libId;
    @ApiModelProperty(value = "图片url")
    private List<String> imgs;
    @ApiModelProperty(value = "人员信息")
    private PersonalInformationVO personalInformation;
}