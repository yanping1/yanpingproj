package com.dkha.api.modules.vo;

import com.dkha.common.entity.vo.position.FaceVO;
import com.dkha.common.entity.vo.position.PositionVO;
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
 * @Title: ApiFaceSearchReturnVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/6 18:34
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "人脸库检索返回", description = "人脸检索")
public class ApiFaceSearchReturnVO {
    private List<ApiFaceSearchFacesVO> faces;
    private PageVO pageVO;
}
