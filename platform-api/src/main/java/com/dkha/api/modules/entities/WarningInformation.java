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
 * 报警表
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WarningInformation对象", description="报警表")
public class WarningInformation extends GovShortBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id_warning_information", type = IdType.ID_WORKER_STR)
    private String idWarningInformation;

    @ApiModelProperty(value = "布控id")
    private String idControlTask;

    @ApiModelProperty(value = "库id")
    private String idFactory;

    @ApiModelProperty(value = "摄像头id")
    private String idFaceBayonet;

    @ApiModelProperty(value = "背景图片")
    private String faceBgurl;

    @ApiModelProperty(value = "人脸坐标位置")
    private String faceRect;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

    @ApiModelProperty(value = "相似度")
    private Double score;

    @ApiModelProperty(value = "额外信息")
    private String extraMeta;



}
