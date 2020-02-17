package com.dkha.api.sdk.wy.modules.vo.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: Position
 * @Package com.dkha.wy.modules.vo.param
 * @author: panhui
 * @date: 2019/11/19 15:17
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Position {
    private String faceId;
    private Rect rect;
    private P5s p5s;
    private String extId;
}
