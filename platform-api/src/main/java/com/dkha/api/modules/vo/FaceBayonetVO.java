package com.dkha.api.modules.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: FaceBayonetVO
 * @Package com.dkha.api.modules.vo
 * @author: yangjun
 * @date: 2019/12/5 17:57
 * @Copyright: 成都电科慧安
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FaceBayonet对象", description="摄像头表")
public class FaceBayonetVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String carmeraId;

    @ApiModelProperty(value = "摄像头名称")
    private String name;

    @ApiModelProperty(value = "摄像头地址")
    private String url;

    @ApiModelProperty(value = "摄像头类型")
    private String type;

    @ApiModelProperty(value = "额外信息")
    private String extraMeta;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}