package com.dkha.api.modules.entities;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName:
 * @Description:(please write your description)
 * @author: {yangjun}
 * @date:
 * @Copyright: 成都电科惠安
 */
@Data
public class GovShortBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否有效 Y有效 N无效 作逻辑删除使用")
    private String isValid;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
