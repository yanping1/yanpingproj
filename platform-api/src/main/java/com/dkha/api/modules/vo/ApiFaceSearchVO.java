package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ApiFaceSearcVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/6 18:28
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人脸检索类", description="人脸检索类")
public class ApiFaceSearchVO {
    @ApiModelProperty(value = "人脸url列表")
    private String url;
    @ApiModelProperty(value = "人脸库列表")
    private List<String> libraryIds;
    @ApiModelProperty(value = "每个库最大返回结果数, 0~100")
    private Double minScore;
    @ApiModelProperty(value = "分页数据")
    private PageVO pageVO;
}
