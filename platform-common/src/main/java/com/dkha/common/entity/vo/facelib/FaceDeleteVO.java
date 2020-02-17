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
 * @Description: 人脸删除请求
 * All rights 成都电科慧安
 * @Title: FaceDeleteVO
 * @Package com.dkha.common.entity.vo.facelib
 * @author: yangjun
 * @date: 2019/11/27 11:16
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "人脸删除通用接口请求参数类", description = "用于和底层传输统一参数格式")
public class FaceDeleteVO {
    @ApiModelProperty(value = "库id")
    private String libId;
    @ApiModelProperty(value = "人脸id")
    private List<String> faceIds;
    @ApiModelProperty(value = "featId 对应百度底层中的userId")
    private String featId;

}