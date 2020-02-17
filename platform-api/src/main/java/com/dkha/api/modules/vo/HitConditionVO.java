package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(预警任务中过滤参数)
 * All rights 成都电科慧安
 * @Title: HitConditionVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/11/29 17:08
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="预警任务中过滤参数", description="业务层调用返回的数据")
public class HitConditionVO {

    @ApiModelProperty(value = "当前页")
    private Integer pageNo;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(value = "开始时间戳")
    private Long startTimestamp;

    @ApiModelProperty(value = "结束时间戳")
    private Long stopTimestamp;

    @ApiModelProperty(value = "排序时间戳默认DESC传递ASC")
    private String order;
}
