package com.dkha.api.sdk.wy.modules.vo.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: HeadBean
 * @Package com.dkha.wy.modules.vo
 * @author: panhui
 * @date: 2019/11/19 9:39
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HeadBean {
    private String protId;
    private int wappId;
    private int serId;
    private String version;
    private int ret;
    private String errInfo;
}
