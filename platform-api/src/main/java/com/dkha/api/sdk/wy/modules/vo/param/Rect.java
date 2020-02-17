package com.dkha.api.sdk.wy.modules.vo.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: Rect
 * @Package com.dkha.wy.modules.vo.param
 * @author: panhui
 * @date: 2019/11/19 15:19
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rect {
    private Integer left;
    private Integer top;
    private Integer right;
    private Integer bottom;
    private Double fscore;
}
