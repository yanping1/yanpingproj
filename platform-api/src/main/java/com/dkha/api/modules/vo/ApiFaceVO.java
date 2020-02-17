package com.dkha.api.modules.vo;

import com.dkha.common.entity.vo.position.FeatureVO;
import com.dkha.common.entity.vo.position.PositionVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description: TODO(用于接口返回实体)
 * All rights 成都电科慧安
 * @Title: FaceVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/2 14:11
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="预警报警组装给业务层的数据实体类", description="预警报警组装给业务层的数据实体类")
public class ApiFaceVO {

    @ApiModelProperty(value="人脸坐标")
    private PositionVO faceRect;

    @ApiModelProperty(value="人脸比中信息")
    private List<ComparisonVO> compare;

    @ApiModelProperty(value="特征")
    private FeatureVO feature;

}
