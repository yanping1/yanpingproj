package com.dkha.api.services;

import com.dkha.api.modules.vo.ApiFaceOneSearchReturnFaceVO;
import com.dkha.api.modules.vo.ApiFaceSearchVO;
import com.dkha.common.entity.vo.facelib.FaceLibaryReturnVO;
import com.dkha.common.entity.vo.facelib.OnecompareOneVO;

import com.dkha.common.entity.vo.position.GroupRelation;
import com.dkha.common.entity.vo.search.FaceCheckVO;
import com.dkha.common.entity.vo.search.FaceCompareVO;

import java.util.List;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: IFindfaceService
 * @Package com.dkha.api.services
 * @author: yangjun
 * @date: 2019/11/28 17:42
 * @Copyright: 成都电科慧安
 */
public interface IFindfaceService {
    /**
     * 人脸库检索
     * @param apiFaceSearcVO
     * @return
     */
    FaceLibaryReturnVO faceFindLibary(ApiFaceSearchVO apiFaceSearcVO);

    /**
     * 人脸检测
     * @param url 背景图片
     * @return
     */
    FaceLibaryReturnVO faceDetection(String url);
    /**
     * 人脸分组检测
     * @param faceCheckVO 人脸图片
     * @return
     */
    List<GroupRelation> faceGroupDetection(FaceCheckVO faceCheckVO);

    /**
     * 1:1
     * @param faceCompareVO 人脸图片
     * @return
     */
    ApiFaceOneSearchReturnFaceVO OnecompareOne(FaceCompareVO faceCompareVO);
}