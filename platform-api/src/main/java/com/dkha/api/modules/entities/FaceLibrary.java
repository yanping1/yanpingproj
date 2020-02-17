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
 * 库表
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FaceLibrary对象", description="库表")
public class FaceLibrary extends GovShortBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id_factory", type = IdType.ID_WORKER_STR)
    private String idFactory;

    @ApiModelProperty(value = "库名称")
    private String factoryName;

    @ApiModelProperty(value = "库类型")
    private String factoryType;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "额外信息")
    private String extraMeta;


}
