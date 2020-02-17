package com.dkha.api.modules.vo;

import com.dkha.common.entity.vo.warning.WarningVO;
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
 * @Title: ESReturnVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/11/29 18:30
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="查询es返回数据的实体类", description="业务层调用返回的数据")
public class EsReturnVO {

    @ApiModelProperty(value="警报数据")
    private WarningVO content;

    @ApiModelProperty(value="总数")
    private Long totalElements;
}
