package com.dkha.api.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.common.exception.DkExceptionHandler;
import com.dkha.api.modules.entities.Portrait;
import com.dkha.api.mappers.PortraitMapper;
import com.dkha.api.modules.vo.ImagesVO;
import com.dkha.api.modules.vo.PagePortraitVO;
import com.dkha.api.services.IFindfaceService;
import com.dkha.api.services.IPortraitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkha.common.entity.vo.ApiVO;
import com.dkha.common.entity.vo.facelib.FaceDeleteVO;
import com.dkha.common.entity.vo.facelib.FaceLibaryReturnVO;
import com.dkha.common.entity.vo.facelib.FaceLibaryVO;
import com.dkha.common.entity.vo.facelib.PersonalInformationVO;
import com.dkha.common.entity.vo.position.FaceVO;
import com.dkha.common.entity.vo.position.PositionVO;
import com.dkha.common.enums.ApiQueryEnum;
import com.dkha.common.enums.YNEnums;
import com.dkha.common.fileupload.MinioUtil;
import com.dkha.common.http.SendUtil;
import com.dkha.common.util.Base64ImageUtils;
import com.dkha.common.validate.UtilValidate;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * <p>
 * 图片表 服务实现类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Service
@Slf4j
public class PortraitServiceImpl extends ServiceImpl<PortraitMapper, Portrait> implements IPortraitService {


    @Resource
    private PortraitMapper portraitMapper;

    @Autowired
    private SendUtil sendUtil;

    @Autowired
    private Gson gson;

    @Value("${wy.isPosition}")
    private Boolean isPosition;


    @Autowired
    private MinioUtil minioUtil;

    @Override
    @Transactional
    public Portrait addPortrait(ImagesVO imagesVO) {
        try {
            //todo 调用微云特征添加
            /**人脸特征添加 （保存人脸坐标 特征id ）*/
            List<String> list = new ArrayList<>();
            list.add(imagesVO.getUrl());
            FaceLibaryVO faceLibaryVO = new FaceLibaryVO();
            ApiVO apiVO = getApiVO(0, "特征添加", ApiQueryEnum.FACE_FEATURE_ADD.getValue());
            faceLibaryVO.setImgs(list);
            faceLibaryVO.setLibId(imagesVO.getLibraryId());
            apiVO.setData(faceLibaryVO);
            ApiVO resultMap = sendUtil.SendWy(apiVO);
            if (resultMap.getCode().intValue() != 0) {
                throw new DkExceptionHandler("新增人脸失败,维云返回接口参数有误:" + resultMap.getMessage());
            }
            String json = gson.toJson(resultMap.getData());
            FaceLibaryReturnVO faceLibaryReturnVO = gson.fromJson(json, FaceLibaryReturnVO.class);
            if (null == faceLibaryReturnVO.getFaces() || faceLibaryReturnVO.getFaces().size() == 0) {
                throw new DkExceptionHandler("新增人脸失败,没有检测到人脸");
            }
            //只有单个人脸的时候直接绑定身份信息
            Date myDate = new Date();
            if (faceLibaryReturnVO.getFaces().size() != 1) {
                throw new DkExceptionHandler("新增人脸失败,人脸数量不正常");
            }
            FaceVO faceVO = faceLibaryReturnVO.getFaces().get(0);
            Portrait portrait = new Portrait();
            BeanUtils.copyProperties(imagesVO, portrait);
            portrait.setBackgroundUrl(imagesVO.getUrl());
            portrait.setUrl(imagesVO.getUrl());
            portrait.setSex(imagesVO.getGender());
            portrait.setIdFactory(imagesVO.getLibraryId());
            portrait.setFeatId(faceVO.getFeatId());
            //生日获取
//            if (null != imagesVO.getIdCard() && !imagesVO.getIdCard().equals("") && imagesVO.getIdCard().length() == 18) {
//                try {
//                    Date date = new Date();
//                    date.setYear(Integer.parseInt(imagesVO.getIdCard().substring(6, 10)) - 1900);
//                    date.setMonth(Integer.parseInt(imagesVO.getIdCard().substring(10, 12)) - 1);
//                    date.setDate(Integer.parseInt(imagesVO.getIdCard().substring(12, 14)));
//                    portrait.setBirthDate(date);
//                } catch (Exception e) {
//                    throw new DkException("身份信息有误");
//                }
//            }
            portrait.setCreateTime(myDate);
            portrait.setUpdateTime(myDate);
            portrait.setIsValid(YNEnums.YES.code);
            PositionVO positionVo = faceVO.getPosition();
            BufferedImage bufferedImage = ImageIO.read(new URL(portrait.getBackgroundUrl()));
            if (isPosition) {
                positionVo = Base64ImageUtils.deelPostion(positionVo, bufferedImage.getWidth(), bufferedImage.getHeight());
            }
            if (null != bufferedImage) {
                InputStream inputStream = Base64ImageUtils.encodeHeadImage(bufferedImage, positionVo);
                if (null != inputStream) {
                    //minio上传文件
                    String suff = portrait.getBackgroundUrl().substring(portrait.getBackgroundUrl().lastIndexOf("."));
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "").toString();
                    int hash = uuid.hashCode();
                    if (hash < 0) {
                        hash = 0 - hash;
                    }
                    JSONObject jsonObject = minioUtil.uploadFiles(inputStream, String.valueOf(hash % 500), uuid, suff);
                    log.error("上传成功：{}", jsonObject.toJSONString());
                    if (null != jsonObject && jsonObject.get("flag").equals("0")) {
                        portrait.setUrl(jsonObject.get("url").toString());
                    }
                }
            }
            portrait.setFaceRect(gson.toJson(positionVo));
            portrait.setIdFaceid(faceVO.getFaceId());
            Integer res = portraitMapper.insert(portrait);
            if (res.intValue() != 1) {
                ApiVO returnVo = null;
                try {//删除特征
                    ApiVO apidelVO = getApiVO(0, "人脸特征删除", ApiQueryEnum.FACE_FEATURE_DELETE.getValue());
                    FaceDeleteVO faceDeleteVO = new FaceDeleteVO();
                    faceDeleteVO.setLibId(imagesVO.getLibraryId());
                    List<String> idsList = new ArrayList<>();
                    faceLibaryReturnVO.getFaces().forEach(a ->
                    {
                        idsList.add(a.getFaceId());
                    });
                    faceDeleteVO.setFaceIds(idsList);
                    apidelVO.setData(faceDeleteVO);
                    returnVo = sendUtil.SendWy(apidelVO);
                    log.error("维云特征库删除特征失败{}", gson.toJson(returnVo));
                } catch (Exception ex) {
                    log.error("维云特征库删除特征失败{}", gson.toJson(returnVo));
                }
                throw new DkExceptionHandler("上传失败");
            }
            return portrait;
        } catch (Exception e) {
            throw new DkExceptionHandler(e.getMessage());
        }
    }

    @Override
    public Portrait builtPortrait(Portrait portrait) {
        try {
            Integer size = portraitMapper.updateById(portrait);
            if (size.intValue() != 0) {
                return portrait;
            }
        } catch (Exception e) {
        }
        return null;
    }
    //支持一张图多个人脸的写入
//    @Override
//    @Transactional
//    public List<Portrait> addPortrait(ImagesVO imagesVO) {
//        try {
//            //todo 调用微云特征添加
//            /**人脸特征添加 （保存人脸坐标 特征id ）*/
//            List<String> list = new ArrayList<>();
//            list.add(imagesVO.getUrl());
//            FaceLibaryVO faceLibaryVO = new FaceLibaryVO();
//            ApiVO apiVO = getApiVO(0, "特征添加", ApiQueryEnum.FACE_FEATURE_ADD.getValue());
//            faceLibaryVO.setImgs(list);
//            faceLibaryVO.setLibId(imagesVO.getLibraryId());
//            apiVO.setData(faceLibaryVO);
//            ApiVO resultMap = sendUtil.SendWy(apiVO);
//            if (resultMap.getCode().intValue() != 0) {
//                throw new DkException("新增人脸失败,维云返回接口参数有误:" + resultMap.getMessage());
//            }
//            String json = gson.toJson(resultMap.getData());
//            FaceLibaryReturnVO faceLibaryReturnVO = gson.fromJson(json, FaceLibaryReturnVO.class);
//            if (null == faceLibaryReturnVO.getFaces() || faceLibaryReturnVO.getFaces().size() == 0) {
//                throw new DkException("新增人脸失败,没有检测到人脸");
//            }
//            //只有单个人脸的时候直接绑定身份信息
//            List<Portrait> portList = new ArrayList<>();
//            Date myDate = new Date();
//            if (faceLibaryReturnVO.getFaces().size() == 1) {
//                Portrait portrait = new Portrait();
//                BeanUtils.copyProperties(imagesVO, portrait);
//                portrait.setBackgroundUrl(imagesVO.getUrl());
//                portrait.setUrl(imagesVO.getUrl());
//                portrait.setSex(imagesVO.getGender());
//                portrait.setIdFactory(imagesVO.getLibraryId());
//                //生日获取
//                if (null != imagesVO.getIdCard() && !imagesVO.getIdCard().equals("") && imagesVO.getIdCard().length() == 18) {
//                    try {
//                        Date date = new Date();
//                        date.setYear(Integer.parseInt(imagesVO.getIdCard().substring(6, 10)) - 1900);
//                        date.setMonth(Integer.parseInt(imagesVO.getIdCard().substring(10, 12)) - 1);
//                        date.setDate(Integer.parseInt(imagesVO.getIdCard().substring(12, 14)));
//                        portrait.setBirthDate(date);
//                    } catch (Exception e) {
//                        throw new DkException("身份信息有误");
//                    }
//                }
//                portrait.setCreateTime(myDate);
//                portrait.setUpdateTime(myDate);
//                portrait.setIsValid(YNEnums.YES.code);
//                PositionVO positionVo = faceLibaryReturnVO.getFaces().get(0).getPosition();
//                BufferedImage bufferedImage = ImageIO.read(new URL(portrait.getBackgroundUrl()));
//                if (isPosition) {
//                    positionVo = Base64ImageUtils.deelPostion(positionVo, bufferedImage.getWidth(), bufferedImage.getHeight());
//                }
//                if (null != bufferedImage) {
//                    InputStream inputStream = Base64ImageUtils.encodeHeadImage(bufferedImage, positionVo);
//                    if (null != inputStream) {
//                        //minio上传文件
//                        String suff = portrait.getBackgroundUrl().substring(portrait.getBackgroundUrl().lastIndexOf("."));
//                        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toString();
//                        JSONObject jsonObject = minioUtil.uploadFile(inputStream, uuid, suff);
//                        log.error("上传成功：{}", jsonObject.toJSONString());
//                        if (null != jsonObject && jsonObject.get("flag").equals("0")) {
//                            portrait.setUrl(jsonObject.get("url").toString());
//                        }
//                    }
//                }
//                portrait.setFaceRect(gson.toJson(positionVo));
//                portrait.setIdFaceid(faceLibaryReturnVO.getFaces().get(0).getFaceId());
//                portList.add(portrait);
//            } else {
//                faceLibaryReturnVO.getFaces().forEach(va ->
//                {
//                    Portrait portrait = new Portrait();
//                    portrait.setCreateTime(myDate);
//                    portrait.setUpdateTime(myDate);
//                    portrait.setIsValid(YNEnums.YES.code);
//                    portrait.setIdFactory(imagesVO.getLibraryId());
//                    portrait.setBackgroundUrl(va.getBgImg());
//                    portrait.setUrl(va.getBgImg());
//                    //坐标使用扩大的坐标
//                    PositionVO positionVo = va.getPosition();
//                    BufferedImage bufferedImage = null;
//                    try {
//                        bufferedImage = ImageIO.read(new URL(portrait.getBackgroundUrl()));
//                    } catch (IOException e) {
//                    }
//                    if (isPosition) {
//                        positionVo = Base64ImageUtils.deelPostion(positionVo, bufferedImage.getWidth(), bufferedImage.getHeight());
//                    }
//                    if (null != bufferedImage) {
//                        InputStream inputStream = Base64ImageUtils.encodeHeadImage(bufferedImage, positionVo);
//                        if (null != inputStream) {
//                            //minio上传文件
//                            String suff = portrait.getBackgroundUrl().substring(portrait.getBackgroundUrl().lastIndexOf("."));
//                            String uuid = UUID.randomUUID().toString().replaceAll("-", "").toString();
//                            JSONObject jsonObject = null;
//                            try {
//                                jsonObject = minioUtil.uploadFile(inputStream, uuid, suff);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            log.error("上传成功：{}", jsonObject.toJSONString());
//                            if (null != jsonObject && jsonObject.get("flag").equals("0")) {
//                                portrait.setUrl(jsonObject.get("url").toString());
//                            }
//                        }
//                    }
//                    portrait.setFaceRect(gson.toJson(positionVo));
//                    portrait.setIdFaceid(va.getFaceId());
//                    portList.add(portrait);
//                });
//            }
//            AtomicReference<Integer> mark = new AtomicReference<>(0);
//            portList.forEach(a ->
//            {
//                mark.updateAndGet(v -> v + portraitMapper.insert(a));
//            });
//            if (mark.get().intValue() != portList.size()) {
//
//                ApiVO returnVo=null;
//                try {//删除特征
//                    ApiVO apidelVO = getApiVO(0, "人脸特征删除", ApiQueryEnum.FACE_FEATURE_DELETE.getValue());
//                    FaceDeleteVO faceDeleteVO = new FaceDeleteVO();
//                    faceDeleteVO.setLibId(imagesVO.getLibraryId());
//                    List<String> idsList=new ArrayList<>();
//                    portList.forEach(a ->
//                    {
//                        idsList.add(a.getIdFactory());
//                    });
//                    faceDeleteVO.setFaceIds(idsList);
//                    apidelVO.setData(faceDeleteVO);
//                   returnVo=sendUtil.SendWy(apidelVO);
//                    log.error("维云特征库删除特征失败{}",gson.toJson(returnVo));
//                }catch (Exception ex){log.error("维云特征库删除特征失败{}",gson.toJson(returnVo));}
//
//                throw new DkException("上传失败");
//            }
//            return portList;
//        } catch (Exception e) {
//            throw new DkException("新增人脸失败");
//        }
//    }


    @Override
    public Boolean deletePortrait(Portrait portrait) {
        try {
            portrait.setIsValid(YNEnums.NO.code);
            List<String> list = new ArrayList<>();
            list.add(portrait.getIdFaceid());
            FaceDeleteVO faceDeleteVO = new FaceDeleteVO();
            faceDeleteVO.setLibId(portrait.getIdFactory());
            faceDeleteVO.setFaceIds(list);
            faceDeleteVO.setFeatId(portrait.getFeatId());
            ApiVO apiVO = getApiVO(0, "特征删除", ApiQueryEnum.FACE_FEATURE_DELETE.getValue());
            apiVO.setData(faceDeleteVO);
            ApiVO apiReturn = sendUtil.SendWy(apiVO);
            if (apiReturn.getCode().intValue() == 0) {
                int count = portraitMapper.deleteById(portrait.getIdPortrait());
                if (count > 0) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
        }
        return false;
    }


    @Override
    public Page<Portrait> getPortrait(PagePortraitVO pagePortraitVO) {
        /**创建page对象*/
        Page<Portrait> page = new Page<>(pagePortraitVO.getPage().getPageNo(), pagePortraitVO.getPage().getPageSize());
        OrderItem orderItem = (pagePortraitVO.getPage().getOrder().equalsIgnoreCase("DESC") ? OrderItem.desc("create_time") : OrderItem.asc("create_time"));
        page.setOrders(Arrays.asList(orderItem));
        /**设置模糊查询参数*/
        List<Portrait> list = portraitMapper.getPortrait(page, pagePortraitVO.getLibraryId());
        page.setRecords(list);
        return page;
    }

    @Override
    public Portrait getPortraitByIdFaceid(String idFaceid, String idFactory) {
        try {
            Portrait portrait = portraitMapper.getPortraitByIdFaceid(idFaceid, idFactory);
            return portrait;
        } catch (Exception e) {
            throw new DkExceptionHandler("查询人像失败");
        }
    }

    @Override
    public Portrait getPortraitById(String id) {
        try {
            Portrait portrait = portraitMapper.getPortraitByIdIdPortrait(id);
            return portrait;
        } catch (Exception e) {
            throw new DkExceptionHandler("查询人像失败");
        }
    }

    @Autowired
    private IFindfaceService iFindfaceService;

    /**
     * 检测人脸数量 true
     *
     * @param url
     * @return
     */
    @Override
    public Integer cheackPopNumber(String url) {
        try {
//            FaceCheckVO faceCheckVO = new FaceCheckVO();
            FaceLibaryReturnVO faceLibaryReturnVO = iFindfaceService.faceDetection(url);
            if (null == faceLibaryReturnVO) {
                return 0;
            }
            Integer returnLength = faceLibaryReturnVO.getFaces().size();
            return returnLength;
        } catch (Exception e) {
            return 0;
        }
    }


    public ApiVO getApiVO(Integer code, String message, String cmd) {
        ApiVO apiVO = new ApiVO();
        apiVO.setCode(code);
        apiVO.setMessage(message);
        apiVO.setCmd(cmd);
        return apiVO;
    }

    public PersonalInformationVO getPersonalInformationVO(Portrait portrait) {
        PersonalInformationVO personalInformationVO = new PersonalInformationVO();
        personalInformationVO.setSex(portrait.getSex());//性别
        personalInformationVO.setAge(null);//年龄
        personalInformationVO.setName(portrait.getName());//姓名
        personalInformationVO.setIdCard(portrait.getIdCard());//身份证
        personalInformationVO.setFeature(portrait.getFeature());//特征
        return personalInformationVO;
    }
}
