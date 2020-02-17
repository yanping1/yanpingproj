package com.dkha.common.entity.vo.facelib;

import com.dkha.common.entity.vo.position.FaceCompareVO;
import com.dkha.common.entity.vo.position.FaceVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: OnecompareOneVO
 * @Package com.dkha.common.entity.vo.facelib
 * @author: yangjun
 * @date: 2019/12/6 16:17
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="比中结果", description="用于和底层传输统一参数格式")
public class OnecompareOneVO {
    @ApiModelProperty(value = "相似度")
    private Double hitSimilarity;
    private List<FaceCompareVO> faces;

}