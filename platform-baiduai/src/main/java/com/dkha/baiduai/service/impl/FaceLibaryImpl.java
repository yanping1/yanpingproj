package com.dkha.baiduai.service.impl;

import com.baidu.aip.face.AipFace;
import com.dkha.baiduai.common.constant.ErrorEnum;
import com.dkha.baiduai.common.exception.DkException;
import com.dkha.baiduai.service.AuthService;
import com.dkha.baiduai.service.FaceLibary;
import com.dkha.common.entity.vo.ApiVO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author hechenggang
 * @Date 2019/12/25 17:03
 */
@Service
public class FaceLibaryImpl implements FaceLibary {

    @Autowired
    private AuthService authService;
    @Autowired

    private AipFace aipFace;

    @Override
    public ApiVO groupAdd(ApiVO apiVO) {
        if (apiVO == null || apiVO.getData() == null) {
            throw new DkException("参数为空");
        }
        ApiVO result = new ApiVO(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg());
        JSONObject data = new JSONObject(apiVO);
        String libIds = data.getString("libIds");
        JSONObject res = aipFace.groupAdd(libIds, new HashMap<>());
        if (ErrorEnum.SUCCESS.getCode() == (int) res.get("error_code")) {
            result.setData(libIds);
        }

        return result;
    }

    @Override
    public ApiVO groupDelete(ApiVO apiVO) {
        if (apiVO == null || apiVO.getData() == null) {
            throw new DkException("参数为空");
        }
        ApiVO result = new ApiVO(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg());
        JSONObject data = new JSONObject(apiVO);
        String libIds = data.getString("libIds");
        JSONObject res = aipFace.groupDelete(libIds, new HashMap<>());
        if (ErrorEnum.SUCCESS.getCode() == (int) res.get("error_code")) {
            result.setData(libIds);
        }
        return result;
    }
}
