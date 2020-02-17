package com.dkha.api.modules.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 * 摄像头表
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FaceBayonet对象", description="摄像头表")
public class FaceBayonet extends GovShortBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id_face_bayonet", type = IdType.ID_WORKER_STR)
    private String idFaceBayonet;

    @ApiModelProperty(value = "摄像头名称")
    private String bayonetName;

    @ApiModelProperty(value = "摄像头地址")
    private String bayonetAddress;

    @ApiModelProperty(value = "摄像头类型")
    private String bayonetType;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "额外信息")
    private String extraMeta;
}
