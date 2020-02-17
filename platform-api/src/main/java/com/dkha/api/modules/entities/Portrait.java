package com.dkha.api.modules.entities;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 图片表
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Portrait对象", description="图片表")
@JsonInclude
public class Portrait {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id_portrait", type = IdType.ID_WORKER_STR)
    private String idPortrait;

    @ApiModelProperty(value = "库id")
    private String idFactory;

    @ApiModelProperty(value = "人脸id")
    private String idFaceid;

    @ApiModelProperty(value = "人脸图地址")
    private String url;

    @ApiModelProperty(value = "背景图地址")
    private String backgroundUrl;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "出生日期")
    private Date birthDate;

    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "特征id")
    @TableField("featId")
    private String featId;

    @ApiModelProperty(value = "额外信息")
    private String extraMeta;

    @ApiModelProperty(value = "人脸坐标位置")
    private String faceRect;

    @ApiModelProperty(value = "特征信息")
    private String feature;


    @ApiModelProperty(value = "是否有效 Y有效 N无效 作逻辑删除使用")
    private String isValid;


    @ApiModelProperty(value = "创建人")
    private String createBy;


    @ApiModelProperty(value = "更新人")
    private String updateBy;


    @ApiModelProperty(value = "创建时间")

    private Date createTime;


    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
