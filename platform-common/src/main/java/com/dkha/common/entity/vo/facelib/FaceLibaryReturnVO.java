package com.dkha.common.entity.vo.facelib;

import com.dkha.common.entity.vo.position.FaceVO;
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
 * @Description: 人脸入库返回
 * All rights 成都电科慧安
 * @Title: FaceLibaryReturnVO
 * @Package com.dkha.common.entity.vo.facelib
 * @author: yangjun
 * @date: 2019/11/27 10:15
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人脸入库通用接口返回参数类", description="用于和底层传输统一参数格式")
public class FaceLibaryReturnVO {
    @ApiModelProperty(value = "人脸相关信息")
    private List<FaceVO> faces;
    @ApiModelProperty(value = "库id")
    private String libId;
}