package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
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
 * @Title: ApiAlarmVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/11/29 16:00
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="预警任务参数", description="业务层调用返回的数据")
public class ApiAlarmVO {


    @ApiModelProperty(value="任务id")
    private String taskId;

    @ApiModelProperty(value="任务id列表")
    private List<String> taskIds;

    @ApiModelProperty(value = "摄像头id列表")
    private List<String> cameraIds;

    @ApiModelProperty(value = "查询参数筛选")
    private PageVO page;

    @ApiModelProperty(value = "摄像头id仅用于卡口类型业务")
    private String cameraId;

    @ApiModelProperty(value = "任务类别:0 报警任务，1是视频任务")
    private Integer taskType;
}
