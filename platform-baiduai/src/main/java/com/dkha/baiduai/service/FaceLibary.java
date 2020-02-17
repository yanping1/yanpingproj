package com.dkha.baiduai.service;

import com.dkha.common.entity.vo.ApiVO;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author hechenggang
 * @Date 2019/12/25 17:02
 * 一个用户组即对应api层中的一个人脸库
 */
public interface FaceLibary {
    /**
     * 新增用户组
     * @param apiVO
     * @return
     */
    ApiVO groupAdd(ApiVO apiVO);

    /**
     * 删除用户组
     * @param apiVO
     * @return
     */
    ApiVO groupDelete(ApiVO apiVO);

}
