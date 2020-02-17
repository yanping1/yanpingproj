package com.dkha.api.modules.vo;

import com.dkha.common.entity.vo.position.PositionVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ApiFaceSearchFaceVo
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/9 15:03
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "人脸库检索faces返回", description = "人脸检索")
public class ApiFaceSearchFaceVO {
    @ApiModelProperty(value = "人脸背景图")
    private Double hitSimilarity;
    @ApiModelProperty(value = "比中人脸的人脸id")
    private String faceId;
    @ApiModelProperty(value = "人脸库id")
    private String libraryId;
}
