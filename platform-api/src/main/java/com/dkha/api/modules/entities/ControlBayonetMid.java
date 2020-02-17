package com.dkha.api.modules.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 布控和摄像头中间表
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ControlBayonetMid对象", description="布控和摄像头中间表")
public class ControlBayonetMid extends GovShortBaseEntity {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    @ApiModelProperty(value = "布控id")
    private String idControlTask;

    @ApiModelProperty(value = "摄像头id")
    private String idFaceBayonet;


}
