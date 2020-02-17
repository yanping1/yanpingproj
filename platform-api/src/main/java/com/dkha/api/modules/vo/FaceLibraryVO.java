package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: FaceLibraryVO
 * @Package com.dkha.api.modules.vo
 * @author: yangjun
 * @date: 2019/12/3 15:13
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="特征库", description="特征库信息")
public class FaceLibraryVO {
    @ApiModelProperty(value="库id")
    private String libraryId;
    @ApiModelProperty(value="库名称")
    private String name;
    @ApiModelProperty(value="库类型")
    private String type;
    @ApiModelProperty(value="额外信息")
    private String extraMeta;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}