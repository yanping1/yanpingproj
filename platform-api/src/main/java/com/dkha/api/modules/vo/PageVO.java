package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: PageVO
 * @Package com.dkha.api.modules
 * @author: panhui
 * @date: 2019/12/5 10:13
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="用于列表查询后续的分页", description="用于列表查询后续的分页")
public class PageVO {

    @ApiModelProperty(value = "排序 默认按时间DESC  ASC")
    private String order="DESC";

    @ApiModelProperty(value = "开始时间戳")
    private Long startTimestamp;

    @ApiModelProperty(value = "结束时间戳")
    private Long stopTimestamp;

    @ApiModelProperty(value = "当前页")
    private Integer pageNo=1;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize=10;

    @ApiModelProperty(value = "总数")
    private Long total;
}
