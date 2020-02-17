package com.dkha.baiduai.service.impl;

import com.baidu.aip.face.AipFace;
import com.dkha.baiduai.common.constant.ErrorEnum;
import com.dkha.baiduai.common.constant.ImageTypeEnum;
import com.dkha.baiduai.common.constant.RequestParam;
import com.dkha.baiduai.common.exception.DkException;
import com.dkha.baiduai.service.FaceService;
import com.dkha.baiduai.util.UniqueIDCreater;
import com.dkha.common.entity.vo.ApiVO;
import com.dkha.common.entity.vo.facelib.FaceLibaryReturnVO;
import com.dkha.common.entity.vo.position.FaceVO;
import com.dkha.common.entity.vo.position.PositionVO;
import com.dkha.common.util.Base64ImageUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hechenggang
 * @Date 2019/12/25 16:19
 */
@Service
public class FaceServiceImpl implements FaceService {
    private Logger logger = LoggerFactory.getLogger(FaceService.class);

    @Autowired
    private AipFace aipFace;


    @Override
    public ApiVO addUser(ApiVO apiVO) {
        if (apiVO == null || apiVO.getData() == null) {
            throw new DkException("参数为空");
        }
        JSONObject data = new JSONObject(apiVO);
        //用户组id对应人像库id
        String groupId = data.getJSONObject("data").getString("libId");
        JSONArray image = data.getJSONObject("data").getJSONArray("imgs");
        ApiVO result = new ApiVO(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg());
        List<FaceVO> faces = new ArrayList<>();
        FaceLibaryReturnVO faceLibaryReturnVO = new FaceLibaryReturnVO();

        try {
            for (int i = 0; i < image.length(); i++) {
                String base64 = Base64ImageUtils.encodeImgageToBase64(new URL(image.getString(i)));
                String userId = String.valueOf(UniqueIDCreater.generateID());
                JSONObject res = aipFace.addUser(base64, ImageTypeEnum.BASE64.getValue(), groupId, userId, new HashMap<>());
                if (ErrorEnum.SUCCESS.getCode() == res.getInt(RequestParam.ERROR_CODE.getValue())) {
                    FaceVO faceVO = new FaceVO();
                    Map<String, Object> map = new HashMap<>();
                    //使用face_token作为人脸唯一标识faceId返回
                    faceVO.setFaceId(res.getJSONObject("result").getString(RequestParam.FACE_TOKEN.getValue()));
                    faceVO.setPosition(new PositionVO(res.getJSONObject("result").getJSONObject("location").getInt("left"),
                            res.getJSONObject("result").getJSONObject("location").getInt("top"),
                            res.getJSONObject("result").getJSONObject("location").getInt("width"),
                            res.getJSONObject("result").getJSONObject("location").getInt("height")
                    ));
                    faceVO.setFeatId(userId);
                    faces.add(faceVO);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        faceLibaryReturnVO.setFaces(faces);
        faceLibaryReturnVO.setLibId(groupId);
        result.setData(faceLibaryReturnVO);
        return result;
    }

    @Override
    public ApiVO deleteUser(ApiVO apiVO) {
        if (apiVO == null || apiVO.getData() == null) {

            throw new DkException("参数为空");
        }
        JSONObject data = new JSONObject(apiVO);
        String userId = data.getJSONObject("data").getString("featId");
        String faceToken = data.getJSONObject("data").getJSONArray("faceIds").getString(0);
        String libId = data.getJSONObject("data").getString("libId");
        JSONObject res = aipFace.faceDelete(userId, libId, faceToken, new HashMap<>());
        ApiVO result = new ApiVO();
        result.setCode(res.getInt(RequestParam.ERROR_CODE.getValue()));
        result.setMessage(res.get(RequestParam.ERROR_MSG.getValue()) == null ? null : res.getString(RequestParam.ERROR_MSG.getValue()));
        return result;
    }

    @Override
    public ApiVO search(ApiVO apiVO) {
        if (apiVO == null || apiVO.getData() == null) {
            throw new DkException("参数为空");
        }
        String image = ((Map<String, String>) apiVO.getData()).get(RequestParam.IMAGE.getValue());
        String imageType = ((Map<String, String>) apiVO.getData()).get(ImageTypeEnum.BASE64.getValue());
        String groupId = ((Map<String, String>) apiVO.getData()).get(RequestParam.GROUP_ID.getValue());
        JSONObject res = aipFace.search(image, imageType, groupId, new HashMap<>());
        ApiVO result = new ApiVO(res.getInt(RequestParam.ERROR_CODE.getValue()), (String) res.get(RequestParam.ERROR_MSG.getValue()));
        Map<String, Object> map = new HashMap<>();
        result.setData(res.getJSONObject("result"));
        return result;
    }

    @Override
    public ApiVO detect(ApiVO apiVO) {

        if (apiVO == null || apiVO.getData() == null) {
            throw new DkException("参数为空");
        }
        List<String> images = (List<String>) ((Map<String, Object>) apiVO.getData()).get("imgs");
        for (String image : images) {
            JSONObject res = aipFace.detect(image, ImageTypeEnum.BASE64.getValue(), new HashMap<>());
            JSONArray faceList = res.getJSONArray("face_list");
        }

//        ApiVO result = new ApiVO(res.getInt(RequestParam.ERROR_CODE.getValue()), (String) res.get(RequestParam.ERROR_MSG.getValue()), res.get("face_list"));
        return null;
    }
}


