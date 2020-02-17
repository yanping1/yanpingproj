package com.dkha.api.controllers;

import com.dkha.api.modules.ReturnVO;
import com.dkha.api.modules.entities.Portrait;
import com.dkha.api.modules.vo.*;
import com.dkha.api.services.IFindfaceService;
import com.dkha.api.services.IPortraitService;
import com.dkha.common.entity.vo.facelib.FaceLibaryReturnVO;
import com.dkha.common.entity.vo.position.FeatureVO;
import com.dkha.common.entity.vo.position.GroupRelation;
import com.dkha.common.entity.vo.position.PositionVO;
import com.dkha.common.entity.vo.search.FaceCheckVO;
import com.dkha.common.entity.vo.search.FaceCompareVO;
import com.dkha.common.result.CommonResult;
import com.dkha.common.validate.UtilValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: FindfaceController
 * @Package com.dkha.api.controllers
 * @author: yangjun
 * @date: 2019/11/29 9:49
 * @Copyright: 成都电科慧安
 */
@RestController
@Api(tags="检索管理")
@PropertySource({"classpath:facefilter.properties"})
public class FindfaceController extends ReturnVO {

    @Resource
    private IFindfaceService iFindfaceService;

    @Resource
    private IPortraitService iPortraitService;

    /**
     * 人脸库检索
     * @param
     * @return
     */
    @PostMapping("/faceSearchLib")
    @ApiParam(required = true, name = "faceSearchVO", value = "")
    @ApiOperation(value = "人脸库检索查询")
    public ReturnVO faceFindLibary(@ApiParam(required = true, name = "pageParam", value = "{\n" +
            "  \"url\":\"图片url\",\n" +
            "  \"libraryIds\": [\n" +
            "    \"库id\"\n" +
            "  ],\n" +
            "  \"minScore\": 分值\n," +
            "  \"page\": {\n" +
            "    \"pageSize\": 查看条数（默认10）\n" +
            "  }\n" +
            "}")@RequestBody ApiFaceSearchVO apiFaceSearchVO) {
        if(UtilValidate.isEmpty(apiFaceSearchVO))
        {
            return paramErrorResult(null);
        }
        /**调用微云人脸库检索*/
        FaceLibaryReturnVO faceVO = iFindfaceService.faceFindLibary(apiFaceSearchVO);
        if(UtilValidate.isEmpty(faceVO))
        {
            return failResult("人脸库检索查询失败");
        }
        AtomicLong markLong=new AtomicLong(0L);
        ApiFaceSearchReturnVO apiFaceSearchReturnVO=new ApiFaceSearchReturnVO();
        List<ApiFaceSearchFacesVO> list=new ArrayList<>();
        faceVO.getFaces().forEach(a->
        {
            ApiFaceSearchFacesVO apiFaceSearchFacesVO=new ApiFaceSearchFacesVO();
            apiFaceSearchFacesVO.setFaceRect(a.getPosition());
            apiFaceSearchFacesVO.setUrl(a.getBgImg());
            List<ApiFaceSearchFaceVO> face=new ArrayList<>();
            if(UtilValidate.isNotEmpty(a.getCompareList())) {
                a.getCompareList().forEach(b ->
                {
                    ApiFaceSearchFaceVO faceSearchFaceVO = new ApiFaceSearchFaceVO();
                    Portrait proTrait = iPortraitService.getPortraitByIdFaceid(b.getComparisonFaceId(), b.getLibId());
                    if (null != proTrait) {
                        faceSearchFaceVO.setFaceId(proTrait.getIdPortrait());
                    } else {
                        faceSearchFaceVO.setFaceId(b.getComparisonFaceId());
                    }
                    faceSearchFaceVO.setHitSimilarity(b.getScore());
                    faceSearchFaceVO.setLibraryId(b.getLibId());
                    face.add(faceSearchFaceVO);
                    markLong.set(markLong.get() + 1);
                });
            }
            apiFaceSearchFacesVO.setFaceList(face);
            list.add(apiFaceSearchFacesVO);
        });
        apiFaceSearchVO.getPageVO().setTotal(markLong.get());
        apiFaceSearchReturnVO.setPageVO(apiFaceSearchVO.getPageVO());
        apiFaceSearchReturnVO.setFaces(list);
        return successResult(apiFaceSearchReturnVO);
    }
    /**
     * 人脸检测
     * @param
     * @return
     */
    @PostMapping("/faceSearch")
    @ApiParam(required = true, name = "url", value = "url")
    @ApiOperation(value = "人脸检测")
    public ReturnVO faceDetection(@RequestParam(value = "url") String url) {
        /**调用微云人脸检查*/
        if(UtilValidate.isEmpty(url)){
            return paramErrorResult("图片不能为空！");
        }
        FaceLibaryReturnVO faceVO = iFindfaceService.faceDetection(url);
        List<ApiSearchFaceRectVO> list=new ArrayList<>();
        faceVO.getFaces().forEach(e->
        {
            ApiSearchFaceRectVO apiVO=new ApiSearchFaceRectVO();
            apiVO.setFaceRect(e.getPosition());
            apiVO.setUrl(e.getBgImg());
            if(!UtilValidate.isEmpty(e.getFeature()))
            {
                apiVO.setFeatureVO(new FeatureVO(e.getFeature().getAge(),e.getFeature().getGender()));
            }

            list.add(apiVO);
        });
        return successResult(list);
    }
    /**
     * 人脸分组
     * @param faceCheckVO
     * @return
     */
    @PostMapping("/faceGroup")
    @ApiParam(required = true, name = "faceCheckVO", value = "")
    @ApiOperation(value = "人脸分组")
    public ReturnVO faceGroupDetection(@ApiParam(required = true, name = "pageParam", value = "{\n" +
            "  \"imgs\": [\n" +
            "  \"图片url\"\n" +
            "  ],\n" +
            " \"minScore\": 分组阀值\n" +
            "        }")@RequestBody FaceCheckVO faceCheckVO) {
        /**调用微云人脸分组*/
        if(UtilValidate.isEmpty(faceCheckVO) || UtilValidate.isEmpty(faceCheckVO.getImgs()))
        {
            return paramErrorResult(null);
        }
        List<GroupRelation> faceVO = iFindfaceService.faceGroupDetection(faceCheckVO);
        if(UtilValidate.isEmpty(faceVO))
        {
            return notInfoResult("没有查询到数据");
        }
        List<List<ApiFaceSearGroupReturnVO>> list=new ArrayList<>();
        faceVO.forEach(a->
        {
            List<ApiFaceSearGroupReturnVO> myList=new ArrayList<>();
            a.getGroupInfoVO().forEach(b->
            {
                ApiFaceSearGroupReturnVO apiVO=new ApiFaceSearGroupReturnVO();
                apiVO.setFaceRect(new PositionVO(b.getX(),b.getY(),b.getW(),b.getH()));
                apiVO.setHitSimilarity(b.getHitSimilarity());
                apiVO.setUrl(b.getHeadUrl());
                myList.add(apiVO);
            });
            list.add(myList);
        });
        return successResult(list);
    }
    /**
     * 1:1比对oneCompareOne
     * @param
     * @return
     */
    @PostMapping("/faceComparison")
    @ApiParam(required = true, name = "faceCompareVO", value = "")
    @ApiOperation(value = "1:1比对")
    public ReturnVO oneCompareOne(@ApiParam(required = true, name = "faceCompareVO", value = "{\n" +
            "  \"image1\": \"第一张图片地址\",\n" +
            "  \"image2\": \"第二张图片地址\"\n" +
            "        }")@RequestBody FaceCompareVO faceCompareVO) {

        if(UtilValidate.isEmpty(faceCompareVO.getImage1()) || UtilValidate.isEmpty(faceCompareVO.getImage2()))
        {
            return paramErrorResult("图片地址不能为空");
        }
        //检测人脸是否是只有一个人脸
        //添加人脸数量的验证
        Integer popLength1=iPortraitService.cheackPopNumber(faceCompareVO.getImage1());
        Integer popLength2=iPortraitService.cheackPopNumber(faceCompareVO.getImage2());
        if(popLength1.intValue()!=1 || popLength2.intValue()!=1)
        {
            return failResult("比对图片只能存在一张人脸,image1人脸数量"+popLength1+",image2人脸数量"+popLength2);
        }
        /**调用微云人脸分组*/
        ApiFaceOneSearchReturnFaceVO faceVO = iFindfaceService.OnecompareOne(faceCompareVO);
        if(null==faceVO)
        {
            return failResult("比对失败");
        }
        return successResult(faceVO);
    }

}