package com.dkha.api.sdk.wy.modules.vo.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: Scfg
 * @Package com.dkha.wy.modules.vo
 * @author: panhui
 * @date: 2019/11/19 13:38
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Scfg {

    private FaceFilt faceFilt;
    private String groupFilt;
    //人臉庫id列表
    private List<String> libIds;
    private Double minScore;
    private Integer maxRetNb;

}
