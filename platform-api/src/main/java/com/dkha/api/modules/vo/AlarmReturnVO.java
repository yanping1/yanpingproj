package com.dkha.api.modules.vo;

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
 * @Description: TODO(预警报警返回接口)
 * All rights 成都电科慧安
 * @Title: AlarmReturnVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/2 11:16
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "预警报警接口返回给业务层的数据", description = "预警报警接口返回给业务层的数据")
public class AlarmReturnVO {


    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "摄像头id")
    private String cameraId;


    @ApiModelProperty(value = "背景图片")
    private String faceBgUrl;

    @ApiModelProperty(value = "报警唯一id")
    private String alarmId;

    @ApiModelProperty(value = "背景高度")
    private Integer bgHeight;
    @ApiModelProperty(value = "背景宽度")
    private Integer bgWidth;
    @ApiModelProperty(value = "抓拍时间")
    private Long timestamp;

    @ApiModelProperty(value = "抓拍时间")
    private String date;


    @ApiModelProperty(value = "人脸比中信息")
    private List<ApiFaceVO> faces;


}
