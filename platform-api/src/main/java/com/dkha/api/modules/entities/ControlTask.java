package com.dkha.api.modules.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@ApiModel(value="ControlTask对象", description="布控表")
public class ControlTask extends GovShortBaseEntity{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id_control_task", type = IdType.ID_WORKER_STR)
    private String idControlTask;

    @ApiModelProperty(value = "布控阈值")
    private Double controlThreshold;

    @ApiModelProperty(value = "布控名称")
    private String taskName;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

    @ApiModelProperty(value = "额外信息")
    private String extraMeta;

    @TableField(exist = false)
    @ApiModelProperty(value = "摄像头id")
    private List<String> camera;

    @TableField(exist = false)
    @ApiModelProperty(value = "库id")
    private List<String> library;
    @TableField(exist = false)
    @ApiModelProperty(value = "url")
    private String url;
}
