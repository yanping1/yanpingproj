package com.dkha.baiduai.util;

import com.baidu.aip.face.AipFace;
import com.dkha.baiduai.common.constant.APIConstant;

/**
 * @author hechenggang
 * @Date 2019/12/25 18:06
 * 获取AipFace客户端
 */
public class AipFaceClientUtil {

    private AipFaceClientUtil() {
    }

    private static class AipFaceClientHandler {
        private static AipFace aipFace = new AipFace(APIConstant.APP_ID, APIConstant.API_KEY, APIConstant.SECRET_KEY);
    }

    public static AipFace getInstance() {
        return AipFaceClientHandler.aipFace;
    }
}
