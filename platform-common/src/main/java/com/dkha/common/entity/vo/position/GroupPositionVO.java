package com.dkha.common.entity.vo.position;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @version V1.0
 * @Description: 人脸坐标位置
 * All rights 成都电科慧安
 * @Title: PositionVO
 * @Package com.dkha.common.entity.vo.face
 * @author: panhui
 * @date: 2019/11/27 10:36
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="坐标类", description="人脸坐标")
public class GroupPositionVO {
    @ApiModelProperty(value = "人脸对应的id")
    private String faceId;
    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;
    private String headUrl;//头像图片地址
    private String bgUrl;//背景图片
    private double hitSimilarity;//命中的相似度

}
