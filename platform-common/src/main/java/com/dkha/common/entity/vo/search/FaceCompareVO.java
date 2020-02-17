package com.dkha.common.entity.vo.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description: 人脸库检查/人脸分组参数
 * All rights 成都电科慧安
 * @Title: FaceSearchVO
 * @Package com.dkha.common.entity.vo.search
 * @author: yangjun
 * @date: 2019/11/27 10:40
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人脸库检测通用接口参数类", description="用于和底层传输统一参数格式")
public class FaceCompareVO {
    @ApiModelProperty(value = "人脸图片1")
    private String image1;
    @ApiModelProperty(value = "人脸图片2")
    private String image2;
    @ApiModelProperty(value = "拓展字段")
    private String extraMeta;

}