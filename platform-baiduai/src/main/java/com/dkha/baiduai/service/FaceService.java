package com.dkha.baiduai.service;

import com.dkha.common.entity.vo.ApiVO;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author hechenggang
 * @Date 2019/12/25 16:18
 */
public interface FaceService {


    /**
     * 人脸注册
     * @param apiVO 参数列表
     * @return 返回response
     */
    ApiVO addUser(ApiVO apiVO);

    /**
     * 删除人脸
     * @param apiVO
     * @return
     */
    ApiVO deleteUser(ApiVO apiVO);

    /**
     * 检索人脸
     * @param apiVO
     * @return
     */
    ApiVO search(ApiVO apiVO);

    /**
     * 人脸检测
     * @param apiVO
     * @return
     */
    ApiVO detect(ApiVO apiVO);

}
