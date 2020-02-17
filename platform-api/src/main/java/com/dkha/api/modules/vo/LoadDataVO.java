package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: LoadDataVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/16 11:18
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "预警状态接口", description = "预警状态接口")
public class LoadDataVO {
    private Map<String,Object> head;
    private Map<String,String> status;
}
