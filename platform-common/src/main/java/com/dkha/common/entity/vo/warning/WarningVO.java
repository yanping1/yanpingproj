package com.dkha.common.entity.vo.warning;

import com.dkha.common.entity.vo.position.FaceVO;
import com.dkha.common.enums.ApiWarnEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @version V1.0
 * @Description: 预警信息
 * All rights 成都电科慧安
 * @Title: WarningVO
 * @Package com.dkha.common.entity.vo.warning
 * @author: panhui
 * @date: 2019/11/27 9:49
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="预警-查询存es", description="用于和底层传输统一参数格式")
@Document(indexName = "warningvo", type = "doc")
public class WarningVO {
    @ApiModelProperty(value = "报警唯一id")
    @Id
//    @Field(type= FieldType.Keyword)
    private String id;
    @ApiModelProperty(value="时间戳用来排序")
    private Long timestamp;
    @ApiModelProperty(value = "消息日期")
    private String date;
    @ApiModelProperty(value = "任务id")
    private String taskIdCameraId;//task_id_ph_0001
    @ApiModelProperty(value = "api端任务id")
    private String taskId;//task_id_ph
    @ApiModelProperty(value = "摄像头id")
    private String cameraId;//0001
    @ApiModelProperty(value = "背景图片")
    private String bgImg;
    @ApiModelProperty(value = "背景高度")
    private Integer bgHeight;
    @ApiModelProperty(value = "背景宽度")
    private Integer bgWidth;
    @ApiModelProperty(value = "人脸坐标列表")
    private List<FaceVO> faces;
    @ApiModelProperty(value = "是否报警信息：0 未命中 1命中")
    private Integer iswarning= ApiWarnEnum.MISS.getCode();
    @ApiModelProperty(value = "任务类别:0 报警任务，1是视频任务")
    private Integer tasktype;

}
