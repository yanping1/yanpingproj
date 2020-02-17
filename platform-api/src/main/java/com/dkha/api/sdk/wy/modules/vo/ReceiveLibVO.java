package com.dkha.api.sdk.wy.modules.vo;

import com.dkha.api.sdk.wy.modules.vo.param.HeadBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @version V1.0
 * @Description: TODO(接收查询特征库的实体类)
 * All rights 成都电科慧安
 * @Title: ReceiveLibVo
 * @Package com.dkha.wy.modules.vo
 * @author: panhui
 * @date: 2019/11/19 15:37
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiveLibVO {

    //通用
    private HeadBean head;


    //查询特征库返回
    private Map<String,Integer> libIds;
}
