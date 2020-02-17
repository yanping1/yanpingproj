package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ApiFaceOneSearchReturnFaceVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/9 11:42
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "人脸库1：1比对返回", description = "人脸检索")
public class ApiFaceOneSearchReturnFaceVO {

    private Double hitSimilarity;
    private List<ApiFaceOneSearchFaceVO> faces;

    private String extraMeta;
}
