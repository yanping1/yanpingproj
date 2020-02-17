package com.dkha.api.sdk.wy.modules.vo.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: P5s
 * @Package com.dkha.wy.modules.vo.param
 * @author: panhui
 * @date: 2019/11/19 15:50
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class P5s {
    private Integer x1;
    private Integer y1;

    private Integer x2;
    private Integer y2;

    private Integer x3;
    private Integer y3;

    private Integer x4;
    private Integer y4;

    private Integer x5;
    private Integer y5;
}
