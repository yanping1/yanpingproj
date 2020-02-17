package com.dkha.common.entity.vo.position;

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
 * @Title: FeatureVO
 * @Package com.dkha.common.entity.vo.position
 * @author: panhui
 * @date: 2019/12/13 14:42
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="特征类", description="人脸特征类")
public class FeatureVO {

    @ApiModelProperty(value = "人脸检查后年龄")
    private Integer age;
    @ApiModelProperty(value = "人脸检查后性别")
    private String gender ;
}
