package com.dkha.common.entity.vo.position;

import com.dkha.common.entity.vo.facelib.CompareListVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description:
 * All rights 成都电科慧安
 * @Title: faceVO
 * @Package com.dkha.common.entity.vo.face
 * @author: panhui
 * @date: 2019/11/27 10:36
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人脸和坐标相关类", description="人脸和坐标相关类")
public class FaceVO {

    @ApiModelProperty(value = "背景图片")
    private String bgImg;
    @ApiModelProperty(value = "人脸id")
    private String faceId;
    @ApiModelProperty(value = "坐标")
    private PositionVO position;

    @ApiModelProperty(value = "特征信息")
    private FeatureVO feature;

    @ApiModelProperty(value = "特征id (用于百度ai平台中的用户id userId)")
    private String featId;
    @ApiModelProperty(value = "人脸比中结果集 注：人脸入库不需要返回此字段")
    private List<CompareListVO> compareList;

}

