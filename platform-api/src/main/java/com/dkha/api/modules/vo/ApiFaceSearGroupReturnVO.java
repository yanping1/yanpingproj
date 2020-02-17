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
 * @Title: ApiFaceSearGroupReturnVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/9 13:00
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "人脸分组检索返回数据", description = "人脸分组检索返回数据")
public class ApiFaceSearGroupReturnVO {
    private PositionVO faceRect;
    private String url;//背景图片
    private double hitSimilarity;//命中的相似度
}
