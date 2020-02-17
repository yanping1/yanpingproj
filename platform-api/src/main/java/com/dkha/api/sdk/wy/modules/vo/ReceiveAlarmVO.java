package com.dkha.api.sdk.wy.modules.vo;

import com.dkha.api.sdk.wy.modules.vo.param.FaceIds;
import com.dkha.api.sdk.wy.modules.vo.param.HeadBean;
import com.dkha.api.sdk.wy.modules.vo.param.Position;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @version V1.0
 * @Description: TODO(接收预警所用的类)
 * All rights 成都电科慧安
 * @Title: ReceiveAlarmVO
 * @Package com.dkha.wy.modules.vo
 * @author: panhui
 * @date: 2019/11/19 15:31
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiveAlarmVO {
    //通用
    private HeadBean head;
    private Map<String,String> urls;
    private FaceIds faceIds;
    private Map<String, Position> position;
    private String taskId;
    private Object libSchScore;
    private Object attribute;
}
