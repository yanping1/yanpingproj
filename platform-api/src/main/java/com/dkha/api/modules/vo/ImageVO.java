package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ImagesVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/3 15:14
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "用于图片入库接口", description = "用于图片入库接口")
public class ImageVO {

    @ApiModelProperty(value = "需要入库的人脸地址")
    @NotBlank(message = "人脸图片地址不能为空")
    private String url;

    @ApiModelProperty(value = "身份证id可空")
    private String idCard;

    @ApiModelProperty(value = "性别可空")
    private String gender;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "名族")
    private String nation;

    @ApiModelProperty(value = "特征信息")
    private String feature;

    @ApiModelProperty(value = "额外信息")
    private String extraMeta;
}
