package com.dkha.common.entity.vo.control;

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
 * @Title: ControlVO
 * @Package com.dkha.common.entity.vo.control
 * @author: panhui
 * @date: 2019/11/27 10:55
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "布控-新增/删除", description = "用于布控和底层传输统一参数格式")
public class ControlVO {
    @ApiModelProperty(value = "任务id")
    private String taskId_cameraId;
    @ApiModelProperty(value = "任务地址根据下面类型来传递不同的地址比如：rtsp和http")
    private String vdUrl;
    @ApiModelProperty(value = "任务类型VdUrlRtsp rtsp流类型url VdUrlGb28181  gb28181流类型url VdUrlHttp http视频文件类型url")
    private String vdType;
    @ApiModelProperty(value = "库列表id")
    private List<String> libIds;
    @ApiModelProperty(value = "最小分值,取值范围0.00~1.00")
    private Double minScore;
    @ApiModelProperty(value = "每个库最大返回结果数, 0~100")
    private Double maxRetNb;

    @ApiModelProperty(value = "删除任务列表")
    private List<String>  taskIdCameraIds;
}
