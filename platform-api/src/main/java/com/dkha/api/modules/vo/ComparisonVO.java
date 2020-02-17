package com.dkha.api.modules.vo;

import com.dkha.common.entity.vo.position.FeatureVO;
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
 * @Title: ComparisonVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/2 14:42
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="比中人脸信息", description="比中人脸信息")
public class ComparisonVO {
    @ApiModelProperty(value="比中库id")
    private String libraryId;

    @ApiModelProperty(value="比中库中的人脸id")
    private String comparisonFaceId;

    @ApiModelProperty(value="比中分值")
    private Double hitSimilarity;

}
