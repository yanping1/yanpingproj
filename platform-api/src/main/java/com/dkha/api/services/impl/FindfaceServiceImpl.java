package com.dkha.api.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dkha.api.common.exception.DkExceptionHandler;
import com.dkha.api.modules.vo.ApiFaceOneSearchFaceVO;
import com.dkha.api.modules.vo.ApiFaceOneSearchReturnFaceVO;
import com.dkha.api.modules.vo.ApiFaceSearchVO;
import com.dkha.api.services.IFindfaceService;
import com.dkha.common.entity.vo.ApiVO;

import com.dkha.common.entity.vo.facelib.FaceLibaryReturnVO;
import com.dkha.common.entity.vo.facelib.OnecompareOneVO;
import com.dkha.common.entity.vo.position.*;
import com.dkha.common.entity.vo.search.FaceCheckVO;
import com.dkha.common.entity.vo.search.FaceCompareVO;
import com.dkha.common.entity.vo.search.FaceSearchVO;


import com.dkha.common.enums.ApiQueryEnum;
import com.dkha.common.exception.DkException;
import com.dkha.common.fileupload.MinioUtil;
import com.dkha.common.http.HttpUtil;
import com.dkha.common.util.Base64ImageUtils;
import com.dkha.common.validate.UtilValidate;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: FindfaceServiceImpl
 * @Package com.dkha.api.services.impl
 * @author: yangjun
 * @date: 2019/11/28 18:23
 * @Copyright: 成都电科慧安
 */
@Service
public class FindfaceServiceImpl implements IFindfaceService {
    @Resource
    private HttpUtil httpUtil;
    @Resource
    private MinioUtil minioUtil;
    @Value("${wyhttpurlvideo}")
    private String wyhttpurlvideo;
    @Autowired
    private Gson gson;
    /**
     * 取值范围:0.00~1.00(0%~100%)
     */
    @Value("${minScore}")
    private double minScore;
    /**
     * 每个库最大返回结果
     */
    @Value("${maxRetNb}")
    private Integer maxRetNb;

    /**
     * 人脸库检索
     *
     * @param apiFaceSearcVO
     * @return
     */
    @Override
    public FaceLibaryReturnVO faceFindLibary(ApiFaceSearchVO apiFaceSearcVO) {
        /**调用微云人脸库检索*/
        ApiQueryEnum.FACE_LIBARY_SEARCH.getValue();

        FaceSearchVO faceSearchVO = new FaceSearchVO();
        faceSearchVO.setImgs(Arrays.asList(apiFaceSearcVO.getUrl()));
        faceSearchVO.setLibIds(apiFaceSearcVO.getLibraryIds());
        if (UtilValidate.isEmpty(apiFaceSearcVO.getPageVO()) || UtilValidate.isEmpty(apiFaceSearcVO.getPageVO().getPageSize()) || apiFaceSearcVO.getPageVO().getPageSize().intValue() > 100) {
            faceSearchVO.setMaxRetNb(maxRetNb);
        } else {
            faceSearchVO.setMaxRetNb(apiFaceSearcVO.getPageVO().getPageSize());
        }
        if (UtilValidate.isEmpty(apiFaceSearcVO.getMinScore()) || UtilValidate.isEmpty(apiFaceSearcVO.getMinScore())) {
            faceSearchVO.setMinScore(minScore);
        } else {
            faceSearchVO.setMinScore(apiFaceSearcVO.getMinScore());
        }

        ApiVO apiVO = new ApiVO(0, "", ApiQueryEnum.FACE_LIBARY_SEARCH.getValue(), faceSearchVO);
        /**调用微云*/
        ApiVO resultMap = (ApiVO) httpUtil.post(wyhttpurlvideo, apiVO, ApiVO.class);
        if(UtilValidate.isEmpty(resultMap) || resultMap.getCode().intValue()!=0)
        {
            throw  new DkException("底层接口数据失败-"+((null==resultMap)?"":resultMap.getMessage().toString()));
        }
        String json = gson.toJson(resultMap.getData());
        FaceLibaryReturnVO faceLibaryReturnVO = gson.fromJson(json, FaceLibaryReturnVO.class);
        return faceLibaryReturnVO;
    }

    /**
     * 人脸检测 背景图片
     *
     * @param
     * @return
     */
    @Override
    public FaceLibaryReturnVO faceDetection(String url) {
        /**调用微云人脸库检索*/
        ApiQueryEnum.FACE_SEARCH.getValue();
        FaceCheckVO faceCheckVO = new FaceCheckVO();
        faceCheckVO.setMaxRetNb(maxRetNb);
        faceCheckVO.setMinScore(minScore);
        faceCheckVO.setImgs(Arrays.asList(url));
        ApiVO apiVO = new ApiVO(0, "人脸检索", ApiQueryEnum.FACE_SEARCH.getValue(), faceCheckVO);
        /**调用微云*/
        ApiVO resultMap = (ApiVO) httpUtil.post(wyhttpurlvideo, apiVO, ApiVO.class);
        if(UtilValidate.isEmpty(resultMap) || resultMap.getCode().intValue()!=0)
        {
            throw  new DkExceptionHandler("底层接口数据失败-"+((null==resultMap)?"":resultMap.getMessage()));
        }

        String json = gson.toJson(resultMap.getData());
        FaceLibaryReturnVO faceLibaryReturnVO = gson.fromJson(json, FaceLibaryReturnVO.class);
        return faceLibaryReturnVO;
    }

    /**
     * 人脸分组检测
     *
     * @param faceCheckVO 人脸图片
     * @return
     */
    @Override
    public List<GroupRelation> faceGroupDetection(FaceCheckVO faceCheckVO) {
        /**调用微云人脸库检索*/
        ApiQueryEnum.FACE_GROUP_SEARCH.getValue();
        faceCheckVO.setMaxRetNb(maxRetNb);
        faceCheckVO.setMinScore((UtilValidate.isEmpty(faceCheckVO.getMinScore())) ? minScore : faceCheckVO.getMinScore());
        ApiVO apiVO = new ApiVO(0, "人脸库分组", ApiQueryEnum.FACE_GROUP_SEARCH.getValue(), faceCheckVO);
        ApiVO resultMap = (ApiVO) httpUtil.post(wyhttpurlvideo, apiVO, ApiVO.class);
        if(UtilValidate.isEmpty(resultMap) || resultMap.getCode().intValue()!=0)
        {
            throw  new DkExceptionHandler("底层接口数据失败-"+((null==resultMap)?"":resultMap.getMessage()));
        }
        String json = gson.toJson(resultMap.getData());
        GroupVO compareListVO = gson.fromJson(json, GroupVO.class);
        /**分组*/
        List<GroupRelation> groupRelations = groupList(compareListVO, faceCheckVO.getMinScore());
        return groupRelations;
    }

    /**
     * 1:1
     *
     * @param faceCompareVO 人脸图片
     * @return
     */
    @Override
    public ApiFaceOneSearchReturnFaceVO OnecompareOne(FaceCompareVO faceCompareVO) {
        /**调用微云人脸库检索*/
        ApiQueryEnum.FACE_GROUP_SEARCH.getValue();
        ApiVO apiVO = new ApiVO(0, "1:1", ApiQueryEnum.CMDFACE_GROUPONETOONE.getValue(), faceCompareVO);
        ApiVO resultMap = (ApiVO) httpUtil.post(wyhttpurlvideo, apiVO, ApiVO.class);
        if(UtilValidate.isEmpty(resultMap) || resultMap.getCode().intValue()!=0)
        {
            throw  new DkExceptionHandler("底层接口数据失败-"+((null==resultMap)?"":resultMap.getMessage()));
        }
        String json = gson.toJson(resultMap.getData());
        GroupVO compareListVO = gson.fromJson(json, GroupVO.class);

        if(UtilValidate.isEmpty(compareListVO) || UtilValidate.isEmpty( compareListVO.getFScoreComp()))
        {
            return null;
        }
        ApiFaceOneSearchReturnFaceVO faceOneSearchReturnFaceVO=new ApiFaceOneSearchReturnFaceVO();
        faceOneSearchReturnFaceVO.setHitSimilarity(compareListVO.getFScoreComp().get(0).getScore());
        List<ApiFaceOneSearchFaceVO> apiList=new ArrayList<>();
        compareListVO.getFaceIds().forEach(e->
        {
            ApiFaceOneSearchFaceVO apiFaceOneSearchFaceVO=new ApiFaceOneSearchFaceVO();
            apiFaceOneSearchFaceVO.setFaceRect(new PositionVO(e.getX(),e.getY(),e.getW(),e.getH()));
            apiFaceOneSearchFaceVO.setUrl(e.getBgUrl());
            apiList.add(apiFaceOneSearchFaceVO);
        });
        faceOneSearchReturnFaceVO.setFaces(apiList);
        return faceOneSearchReturnFaceVO;
    }

    public List<GroupRelation> groupList(GroupVO groupVO, Double fz) {
        try {
            List<String> list = new ArrayList<>();
            list.add("10101001010100");
//	 * 获取SCFG公共请求体 @param command 当前请求接口命令 @param groupFilt 人脸搜索结构定义-图片人脸检测过滤
//	* @param libIdsFilt 人脸搜索结构定义-人脸分组过滤 @param minScoreFilt 人脸搜索结构定义-搜索特征库列表 @param maxRetNbFilt 人脸搜索结构定义-每个库的最大返回结果数 0-100
//	* @param imgReqType 图片请求类型 @param imgs 图片外部Id对应的图片地址列表/图片列表 @param libId 需要操作的特征库Id

//            Long startTime = System.currentTimeMillis();
//			System.out.println(group.getHead()); //头信息暂时不需要
//			System.out.println(group.getFaceIds());// 人脸id
//			System.out.println(group.getFScoreComp());// 坐标id
//			System.out.println(group.getPosition());// 位置
            // 分组算法
            // 先将比较结果放入map中
            Map<String, Double> compareMap = new HashMap<String, Double>();
            Map<String, GroupPositionVO> positionMap = new HashMap<String, GroupPositionVO>();
            groupVO.getFScoreComp().forEach(x -> {
                compareMap.put(x.getFaceId() + "@" + x.getComparisonFaceId(), x.getScore());
            });
            groupVO.getFaceIds().forEach(y -> {
                positionMap.put(y.getFaceId(), y);
            });
            // 开始进行比较
//			picMap  根据他来拿取图片
            AtomicInteger mark = new AtomicInteger(0);
            List<GroupRelation> faceList = new ArrayList<GroupRelation>();
            for (Map.Entry<String, Imginfo> entry : groupVO.getFaceId().entrySet()) {
                String x = entry.getKey();
                Imginfo y = entry.getValue();
                if (mark.intValue() == 0) {
                    if (y.getFaceIds().size() != 0) {
                        for (int i = 0; i < y.getFaceIds().size(); i++) {
                            List mylist = new ArrayList<GroupPositionVO>();
                            mylist.add(new GroupPositionVO(y.getFaceIds().get(i), positionMap.get(y.getFaceIds().get(i)).getX(),
                                    positionMap.get(y.getFaceIds().get(i)).getY(), positionMap.get(y.getFaceIds().get(i)).getW(), positionMap.get(y.getFaceIds().get(i)).getH(), "", positionMap.get(y.getFaceIds().get(i)).getBgUrl(), positionMap.get(y.getFaceIds().get(i)).getHitSimilarity()));
                            faceList.add(new GroupRelation(y.getFaceIds().get(i), mylist));
                        }
                        mark.set(1);
                    } else {
                        continue;
                    }
                } else {
                    List<String> faceIds = y.getFaceIds();
                    for (int j = 0; j < faceIds.size(); j++) {
                        AtomicInteger mymark = new AtomicInteger(0);
                        for (int i = 0; i < faceList.size(); i++) {
                            if (faceIds.get(j).equals(faceList.get(i).getHeadId())) {
                                continue;
                            }
                            if (!compareMap.containsKey(faceList.get(i).getHeadId() + "@" + y.getFaceIds().get(j))
                                    || !compareMap.containsKey(y.getFaceIds().get(j) + "@" + faceList.get(i).getHeadId())) {
                                Double compareNum = (compareMap.containsKey(y.getFaceIds().get(j) + "@" + faceList.get(i).getHeadId()))
                                        ? compareMap.get(y.getFaceIds().get(j) + "@" + faceList.get(i).getHeadId())
                                        : compareMap.get(faceList.get(i).getHeadId() + "@" + y.getFaceIds().get(j));
                                if (compareNum >= fz) {
                                    mymark.set(1);
                                    faceList.get(i).getGroupInfoVO().add(new GroupPositionVO(faceIds.get(j), positionMap.get(faceIds.get(j)).getX(), positionMap.get(faceIds.get(j)).getY(), positionMap.get(faceIds.get(j)).getW(), positionMap.get(faceIds.get(j)).getH(), "", positionMap.get(faceIds.get(j)).getBgUrl(), compareMap.get(faceList.get(i).getHeadId() + "@" + y.getFaceIds().get(j))));
                                    break;
                                }
                            }
                        }
                        if (mymark.intValue() == 0) {
                            List mylist = new ArrayList<GroupPositionVO>();
                            mylist.add(new GroupPositionVO(faceIds.get(j), positionMap.get(faceIds.get(j)).getX(),
                                    positionMap.get(faceIds.get(j)).getY(), positionMap.get(faceIds.get(j)).getW(), positionMap.get(faceIds.get(j)).getH(), "", positionMap.get(faceIds.get(j)).getBgUrl(), positionMap.get(faceIds.get(j)).getHitSimilarity()));
                            faceList.add(new GroupRelation(faceIds.get(j), mylist));
                        }
                    }
                }
            }
            compareMap.clear();
            positionMap.clear();
//            System.out.println(faceList);
//            System.out.println("人脸数量：" + faceList.size() + "  耗时：" + (System.currentTimeMillis() - startTime));
            if(UtilValidate.isNotEmpty(faceList))
            {
                faceList.stream().filter(a -> UtilValidate.isNotEmpty(a.getGroupInfoVO())).forEach(a -> a.getGroupInfoVO().forEach(b ->
                {
                    PositionVO positionVo = new PositionVO(b.getX(), b.getY(), b.getW(), b.getH());
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = ImageIO.read(new URL(b.getBgUrl()));
                        positionVo = Base64ImageUtils.deelPostion(positionVo, bufferedImage.getWidth(), bufferedImage.getHeight());
                    if (null != bufferedImage) {
                        InputStream inputStream = Base64ImageUtils.encodeHeadImage(bufferedImage, positionVo);
                        if (null != inputStream) {
                            //minio上传文件
                            String suff = b.getBgUrl().substring(b.getBgUrl().lastIndexOf("."));
                            String uuid = UUID.randomUUID().toString().replaceAll("-", "").toString();
                            JSONObject jsonObject = minioUtil.uploadFile(inputStream, uuid, suff);
                            if (null != jsonObject && jsonObject.get("flag").equals("0")) {
                                b.setHeadUrl(jsonObject.get("url").toString());
                            }else{
                                b.setHeadUrl(b.getBgUrl());
                            }
                        }
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
            }
            return faceList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   /* public static List<GroupRelation> groupList(Map<String, String> picMap, Double fz) {

        try {
            List<String> list = new ArrayList<>();
            list.add("10101001010100");
            String s = "{\"head\":{\"protId\":\"CmdFGroup\",\"wappId\":12216230,\"serId\":1918169005,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"faceIds\":{\"img2\":{\"faceIds\":[\"c1bf09631f8a4feeb6be53e1ef32856a\",\"b643c9906c0a49f288223d52fa50fca8\",\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"af77ca14a05b443b977f18d2c25964f3\"],\"reSizeH\":0,\"reSizeW\":0},\"img1\":{\"faceIds\":[\"6c49293e4dbb408b923f5c7f773a27c4\",\"259bda97d9ff4824878e95564b1d21a5\",\"55efb69518b64604a1412e3e87680eca\",\"9aa1bf1bbca4967be152f0643b37b66\"],\"reSizeH\":0,\"reSizeW\":0}},\"position\":{\"259bda97d9ff4824878e95564b1d21a5\":{\"faceId\":\"259bda97d9ff4824878e95564b1d21a5\",\"rect\":{\"left\":717,\"top\":33,\"right\":791,\"bottom\":128,\"fscore\":0.99996376},\"extId\":\"img1\"},\"55efb69518b64604a1412e3e87680eca\":{\"faceId\":\"55efb69518b64604a1412e3e87680eca\",\"rect\":{\"left\":535,\"top\":146,\"right\":590,\"bottom\":213,\"fscore\":0.99996376},\"extId\":\"img1\"},\"b643c9906c0a49f288223d52fa50fca8\":{\"faceId\":\"b643c9906c0a49f288223d52fa50fca8\",\"rect\":{\"left\":717,\"top\":33,\"right\":791,\"bottom\":128,\"fscore\":0.99996376},\"extId\":\"img2\"},\"4a6e6545d1fc4a1486a9ff8c10474f7a\":{\"faceId\":\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"rect\":{\"left\":535,\"top\":146,\"right\":590,\"bottom\":213,\"fscore\":0.99996376},\"extId\":\"img2\"},\"6c49293e4dbb408b923f5c7f773a27c4\":{\"faceId\":\"6c49293e4dbb408b923f5c7f773a27c4\",\"rect\":{\"left\":346,\"top\":201,\"right\":396,\"bottom\":266,\"fscore\":0.99996376},\"extId\":\"img1\"},\"c1bf09631f8a4feeb6be53e1ef32856a\":{\"faceId\":\"c1bf09631f8a4feeb6be53e1ef32856a\",\"rect\":{\"left\":346,\"top\":201,\"right\":396,\"bottom\":266,\"fscore\":0.99996376},\"extId\":\"img2\"},\"9aa1bf1bbca4967be152f0643b37b66\":{\"faceId\":\"9aa1bf1bbca4967be152f0643b37b66\",\"rect\":{\"left\":72,\"top\":118,\"right\":140,\"bottom\":204,\"fscore\":0.99996376},\"extId\":\"img1\"},\"af77ca14a05b443b977f18d2c25964f3\":{\"faceId\":\"af77ca14a05b443b977f18d2c25964f3\",\"rect\":{\"left\":72,\"top\":118,\"right\":140,\"bottom\":204,\"fscore\":0.99996376},\"extId\":\"img2\"}},\"fScoreComp\":[{\"feat1\":\"c1bf09631f8a4feeb6be53e1ef32856a\",\"feat2\":\"b643c9906c0a49f288223d52fa50fca8\",\"score\":0.5241924},{\"feat1\":\"c1bf09631f8a4feeb6be53e1ef32856a\",\"feat2\":\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"score\":0.5109512},{\"feat1\":\"c1bf09631f8a4feeb6be53e1ef32856a\",\"feat2\":\"af77ca14a05b443b977f18d2c25964f3\",\"score\":0.4763277},{\"feat1\":\"c1bf09631f8a4feeb6be53e1ef32856a\",\"feat2\":\"6c49293e4dbb408b923f5c7f773a27c4\",\"score\":0.99999994},{\"feat1\":\"c1bf09631f8a4feeb6be53e1ef32856a\",\"feat2\":\"259bda97d9ff4824878e95564b1d21a5\",\"score\":0.5241924},{\"feat1\":\"c1bf09631f8a4feeb6be53e1ef32856a\",\"feat2\":\"55efb69518b64604a1412e3e87680eca\",\"score\":0.5109512},{\"feat1\":\"c1bf09631f8a4feeb6be53e1ef32856a\",\"feat2\":\"9aa1bf1bbca4967be152f0643b37b66\",\"score\":0.4763277},{\"feat1\":\"b643c9906c0a49f288223d52fa50fca8\",\"feat2\":\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"score\":0.6182962},{\"feat1\":\"b643c9906c0a49f288223d52fa50fca8\",\"feat2\":\"af77ca14a05b443b977f18d2c25964f3\",\"score\":0.6214224},{\"feat1\":\"b643c9906c0a49f288223d52fa50fca8\",\"feat2\":\"6c49293e4dbb408b923f5c7f773a27c4\",\"score\":0.5241924},{\"feat1\":\"b643c9906c0a49f288223d52fa50fca8\",\"feat2\":\"259bda97d9ff4824878e95564b1d21a5\",\"score\":0.99999994},{\"feat1\":\"b643c9906c0a49f288223d52fa50fca8\",\"feat2\":\"55efb69518b64604a1412e3e87680eca\",\"score\":0.6182962},{\"feat1\":\"b643c9906c0a49f288223d52fa50fca8\",\"feat2\":\"9aa1bf1bbca4967be152f0643b37b66\",\"score\":0.6214224},{\"feat1\":\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"feat2\":\"af77ca14a05b443b977f18d2c25964f3\",\"score\":0.55083746},{\"feat1\":\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"feat2\":\"6c49293e4dbb408b923f5c7f773a27c4\",\"score\":0.5109512},{\"feat1\":\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"feat2\":\"259bda97d9ff4824878e95564b1d21a5\",\"score\":0.6182962},{\"feat1\":\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"feat2\":\"55efb69518b64604a1412e3e87680eca\",\"score\":0.99999994},{\"feat1\":\"4a6e6545d1fc4a1486a9ff8c10474f7a\",\"feat2\":\"9aa1bf1bbca4967be152f0643b37b66\",\"score\":0.55083746},{\"feat1\":\"af77ca14a05b443b977f18d2c25964f3\",\"feat2\":\"6c49293e4dbb408b923f5c7f773a27c4\",\"score\":0.4763277},{\"feat1\":\"af77ca14a05b443b977f18d2c25964f3\",\"feat2\":\"259bda97d9ff4824878e95564b1d21a5\",\"score\":0.6214224},{\"feat1\":\"af77ca14a05b443b977f18d2c25964f3\",\"feat2\":\"55efb69518b64604a1412e3e87680eca\",\"score\":0.55083746},{\"feat1\":\"af77ca14a05b443b977f18d2c25964f3\",\"feat2\":\"9aa1bf1bbca4967be152f0643b37b66\",\"score\":0.99999994},{\"feat1\":\"6c49293e4dbb408b923f5c7f773a27c4\",\"feat2\":\"259bda97d9ff4824878e95564b1d21a5\",\"score\":0.5241924},{\"feat1\":\"6c49293e4dbb408b923f5c7f773a27c4\",\"feat2\":\"55efb69518b64604a1412e3e87680eca\",\"score\":0.5109512},{\"feat1\":\"6c49293e4dbb408b923f5c7f773a27c4\",\"feat2\":\"9aa1bf1bbca4967be152f0643b37b66\",\"score\":0.4763277},{\"feat1\":\"259bda97d9ff4824878e95564b1d21a5\",\"feat2\":\"55efb69518b64604a1412e3e87680eca\",\"score\":0.6182962},{\"feat1\":\"259bda97d9ff4824878e95564b1d21a5\",\"feat2\":\"9aa1bf1bbca4967be152f0643b37b66\",\"score\":0.6214224},{\"feat1\":\"55efb69518b64604a1412e3e87680eca\",\"feat2\":\"9aa1bf1bbca4967be152f0643b37b66\",\"score\":0.55083746}]}";
            Gson gson = new Gson();
            GroupVO group = gson.fromJson(s, GroupVO.class);
            Long startTime = System.currentTimeMillis();
            // 分组算法
            // 先将比较结果放入map中
            Map<String, Double> compareMap = new HashMap<String, Double>();
            group.getFScoreComp().forEach(x -> {
                compareMap.put(x.getFeat1() + "@" + x.getFeat2(), x.getScore());
            });
            // 开始进行比较
//			picMap  根据他来拿取图片
            AtomicInteger mark = new AtomicInteger(0);
            List<GroupRelation> faceList = new ArrayList<GroupRelation>();
            group.getFaceIds().forEach((x, y) -> {
                if (mark.intValue() == 0) {
                    if (y.getFaceIds().size() != 0) {
                        y.getFaceIds().forEach(a -> {
                            List mylist = new ArrayList<GroupInfoVO>();
                            if (group.getPosition().containsKey(a)) {
                                PositionVo psv = group.getPosition().get(a);
                                Integer px = psv.getRect().getLeft(), py = psv.getRect().getTop(),
                                        width = (psv.getRect().getRight() - psv.getRect().getLeft()),
                                        height = (psv.getRect().getBottom() - psv.getRect().getTop());
                                mylist.add(new GroupInfoVO(a, "", picMap.get(psv.getExtId()), px, py, width, height));
                            }
                            faceList.add(new GroupRelation(a, mylist));
                        });
                        mark.set(1);
                    } else {
                        return;
                    }
                } else {
                    y.getFaceIds().forEach(a -> {
                        AtomicInteger mymark = new AtomicInteger(0);
                        faceList.forEach(b -> {
                            if (a.equals(b.getHeadId())) {
                                return;
                            }
                            if (!compareMap.containsKey(b.getHeadId() + "@" + a)
                                    || !compareMap.containsKey(a + "@" + b.getHeadId())) {
                                Double compareNum = (compareMap.containsKey(a + "@" + b.getHeadId()))
                                        ? compareMap.get(a + "@" + b.getHeadId())
                                        : compareMap.get(b.getHeadId() + "@" + a);
                                if (compareNum >= fz) {
                                    mymark.set(1);
                                    if (group.getPosition().containsKey(a)) {
                                        PositionVo psv = group.getPosition().get(a);
                                        Integer px = psv.getRect().getLeft(), py = psv.getRect().getTop(),
                                                width = (psv.getRect().getRight() - psv.getRect().getLeft()),
                                                height = (psv.getRect().getBottom() - psv.getRect().getTop());
                                        b.getGroupInfoVO().add(new GroupInfoVO(a, "", picMap.get(psv.getExtId()), px,
                                                py, width, height));
                                    }
                                }
                            }
                        });
                        if (mymark.intValue() == 0) {
                            List mylist = new ArrayList<GroupInfoVO>();
                            if (group.getPosition().containsKey(a)) {
                                PositionVo psv = group.getPosition().get(a);
                                Integer px = psv.getRect().getLeft(), py = psv.getRect().getTop(),
                                        width = (psv.getRect().getRight() - psv.getRect().getLeft()),
                                        height = (psv.getRect().getBottom() - psv.getRect().getTop());
                                mylist.add(new GroupInfoVO(a, "", picMap.get(psv.getExtId()), px, py, width, height));
                            }
                            faceList.add(new GroupRelation(a, mylist));
                        }
                    });
                }
            });
            System.out.println(faceList);
            // 暂时写一个方法用来把分组的图片存到本地来查看分组
            // 去掉注释然后指定一个路径的话就可以把分组的人脸生成图片
//			faceList.forEach(p -> {
//				p.getGroupInfoVO().forEach(h -> {
//					String path = "D:/test/curtvedio/img/compar/res/" + p.getHeadId();
//					File file=new File(path);
//					if(!file.isFile())
//					{
//						file.mkdirs();
//					}
//					BufferedImage bufferedImage;
//					try {
//						bufferedImage = ImageIO.read(new URL(h.getBgpic()));
//						BufferedImage newimg = bufferedImage.getSubimage(h.getX(), h.getY(), h.getWidth(),h.getHeight());
//						ImageUtil.makePic(newimg, path+"/"+h.getHead() + ".jpg");
//					} catch (MalformedURLException e) {
//					} catch (IOException e) {
//					}
//				});
//			});
            System.out.println("人脸数量：" + faceList.size() + "  耗时：" + (System.currentTimeMillis() - startTime));
            return faceList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

   /* public static void main(String[] args) {
        Map<String,String> imgs = new HashMap<>();
        imgs.put("img1","http://192.168.3.240:88/mytest/8.jpg");
        imgs.put("img2","http://192.168.3.240:88/mytest/8.jpg");
        List<GroupRelation> groupRelations = groupList(imgs, 0.8);
        System.out.println(groupRelations);
    }*/
}