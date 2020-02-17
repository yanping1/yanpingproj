package com.dkha.common.entity.vo.position;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: GroupVO
 * @Package com.dkha.common.entity.vo.position
 * @author: yangjun
 * @date: 2019/12/2 15:32
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人脸分组相关类", description="人脸和坐标相关类")
public class GroupVO {
    @ApiModelProperty(value = "人脸对id")
    private Map<String, Imginfo> faceId;
    @ApiModelProperty(value = "人脸对应的坐标")
    private List<GroupPositionVO>  faceIds;
    @ApiModelProperty(value = "人脸比对结果")
    private List<FScoreCompVO> fScoreComp;

}