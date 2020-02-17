package com.dkha.common.entity.vo.facelib;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: 比中返回结果
 * All rights 成都电科慧安
 * @Title: CompareList
 * @Package com.dkha.common.entity.vo.facelib
 * @author: panhui
 * @date: 2019/11/27 15:20
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="比中结果通用", description="用于和底层传输统一参数格式")
public class CompareListVO {
    @ApiModelProperty(value = "比中库里面的人脸id")
    private String comparisonFaceId;

    @ApiModelProperty(value = "库id")
    private String libId;
    @ApiModelProperty(value = "比分")
    private Double score;
}
