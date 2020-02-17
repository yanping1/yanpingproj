package com.dkha.api.modules.vo;

import com.dkha.common.entity.vo.position.FeatureVO;
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
 * @Title: ApiSearchFaceRectVo
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/9 12:36
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人脸检测返回人脸信息", description="人脸检测返回人脸信息")
public class ApiSearchFaceRectVO {
    private PositionVO faceRect;
    private String url;
    private FeatureVO featureVO;
}
