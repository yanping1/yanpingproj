package com.dkha.api.modules.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dkha.api.modules.entities.GovShortBaseEntity;
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

public class ControlTaskVedioVO extends GovShortBaseEntity{

    private static final long serialVersionUID = 1L;
    private String idControlTask;
    @ApiModelProperty(value = "布控阈值")
    private Double threshold;
    @ApiModelProperty(value = "布控名称")
    private String name;
    @TableField(exist = false)
    @ApiModelProperty(value = "url")
    private String url;

    @TableField(exist = false)
    @ApiModelProperty(value = "库id")
    private List<String> libraryId;

    @ApiModelProperty(value = "任务id")
    private String taskId;

}
