package com.dkha.common.entity.vo.libary;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description:  库新增
 * All rights 成都电科慧安
 * @Title: LibaryVO
 * @Package com.dkha.common.entity.vo.libary
 * @author: yangjun
 * @date: 2019/11/27 9:59
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="库通用接口参数类", description="用于和底层传输统一参数格式")
public class LibaryVO {
    @ApiModelProperty(value = "库id列表")
    private List<String> libIds;

}