package com.dkha.api.modules.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dkha.api.modules.entities.GovShortBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 布控表
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)

public class ControlTaskVO extends GovShortBaseEntity{

    private static final long serialVersionUID = 1L;
    private String idControlTask;
    @ApiModelProperty(value = "布控阈值")
    private Double threshold;
    @ApiModelProperty(value = "布控名称")
    private String name;

    @ApiModelProperty(value = "额外信息")
    private String extraMeta;

    @TableField(exist = false)
    @ApiModelProperty(value = "摄像头id")
    private List<String> cameraId;

    @TableField(exist = false)
    @ApiModelProperty(value = "库id")
    private List<String> libraryId;

    @ApiModelProperty(value = "任务id")
    private String taskId;

}
