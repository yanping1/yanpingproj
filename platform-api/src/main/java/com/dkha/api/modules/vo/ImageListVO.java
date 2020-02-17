package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ImagesVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/3 15:14
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "批量图片入库接口", description = "用于图片入库接口")
public class ImageListVO {
    @ApiModelProperty(value = "目标库id")
    @NotNull(message = "目标库id不能为空")
    private String libraryId;

    @ApiModelProperty(value = "人脸相关信息")
    private List<ImageVO> faces;
}