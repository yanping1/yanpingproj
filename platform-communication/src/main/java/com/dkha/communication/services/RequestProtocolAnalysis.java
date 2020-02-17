package com.dkha.communication.services;

import com.alibaba.fastjson.JSON;
import com.dkha.common.entity.vo.ApiVO;
import com.dkha.communication.common.ProtocolAnalysisConst;
import com.dkha.communication.common.WSProtocalConst;
import com.dkha.communication.httpws.cache.ChannelCache;
import com.dkha.communication.httpws.cache.ResponseCache;
import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.httpws.common.WYRequestResponse;
import com.dkha.communication.httpws.factory.SerIdFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @Description: 对请求的协议进行解析，对返回数据进行数据解析
 * @Title: IndexContral
 * @Package com.dkha.communication.services
 * @author: huangyugang
 * @date: 2019/11/27 16:13
 * @Copyright: 成都电科慧安
 */
@Component
@Slf4j
public class RequestProtocolAnalysis {
    /**
     * 请求链接缓存
     */
    @Autowired
    ResponseCache responseCache;

    @Autowired
    WSProtocalConst wsProtocalConst;

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20));
    }

    /**
     * 解析协议信息并推送到消息队列中
     */
    public void transformRequestMSGToQueue(String cmd, Map<String, Object> data, HttpServletResponse response) {
        Map<String, Object> msgDate = requestProtocolRresolution(data, response);
        if (msgDate == null) {
            return;
        } else {
            String msg = JSON.toJSONString(msgDate);
            Map<String, Object> head = (Map<String, Object>) msgDate.get("head");
            String serid = head.get("serId").toString();
            if (msgDate != null) {
                WYRequestResponse requestResponse = new WYRequestResponse();
                requestResponse.setResponse(response);
                requestResponse.setSerid(serid);
                responseCache.set(serid, requestResponse);
                switch (cmd) {
                    //短链接
                    case ProtocolAnalysisConst.CMDLIB_CREATE:
                    case ProtocolAnalysisConst.CMDLIB_PUSH:
                    case ProtocolAnalysisConst.CMDLIB_POP:
                    case ProtocolAnalysisConst.CMDF_SEARCH:
                    case ProtocolAnalysisConst.CMDF_SEARCHFACE:
                    case ProtocolAnalysisConst.CMDFACE_GROUP:
                    case ProtocolAnalysisConst.CMDW_TASKGROUPONETOONE:
                    case ProtocolAnalysisConst.CMDLIB_DELETE: {
                        /*pictureWebSocketClient.sendMessage(msg);*/
                        ChannelCache.sendMessage(WappIdCache.WappIdTypeEnum.PICTURE.code, msg);
                        break;
                    }
                    //长链接
                    case ProtocolAnalysisConst.CMDW_TASKADD:
                    case ProtocolAnalysisConst.CMDW_TASKDEL: {
                        ChannelCache.sendMessage(WappIdCache.WappIdTypeEnum.VIDEO.code, msg);
                        /*videoWebSocketClient.sendMessage(msg);*/
                        break;
                    }
                    default: {
                        return;
                    }
                }
                try {
                    requestResponse.getCountDownLatch().await(120, TimeUnit.SECONDS);
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * 发送警告消息
     *
     * @param apiVO
     * @param response
     */
    public void SendWarningMsg(ApiVO apiVO, HttpServletResponse response) {
        try {
            if (response != null) {
                String msg = JSON.toJSONString(apiVO);
                response.getWriter().print(JSON.toJSONString(apiVO));
                response.getWriter().flush();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 请求协议解析
     *
     * @param requestmap 请求数据
     * @return 返回WS的处理JSON
     */
    public Map<String, Object> requestProtocolRresolution(Map<String, Object> requestmap, HttpServletResponse response) {

        String CMDStr = requestmap.get(ProtocolAnalysisConst.CMD_CONST).toString();
        switch (CMDStr) {
            //创建特征库
            case ProtocolAnalysisConst.CMDLIB_CREATE: {
                return createLibToWSJsonString(requestmap, WappIdCache.WappIdTypeEnum.PICTURE, response);
            }
            //  删除特征库
            case ProtocolAnalysisConst.CMDLIB_DELETE: {
                return deleteLibToWSJsonString(requestmap, WappIdCache.WappIdTypeEnum.PICTURE, response);
            }
            // 人脸新增
            case ProtocolAnalysisConst.CMDLIB_PUSH: {
                return pushFaceinfoLibToWSJsonString(requestmap, WappIdCache.WappIdTypeEnum.PICTURE, response);
            }
            // 人脸删除
            case ProtocolAnalysisConst.CMDLIB_POP: {
                return popFaceInfoLibToWSJsonString(requestmap, WappIdCache.WappIdTypeEnum.PICTURE, response);
            }
            // 人脸库检索
            case ProtocolAnalysisConst.CMDF_SEARCH: {
                return faceSearchLibToWSJsonString(requestmap, 1, WappIdCache.WappIdTypeEnum.PICTURE, response);
            }
            // 人脸检测
            case ProtocolAnalysisConst.CMDF_SEARCHFACE: {
                return faceSearchLibToWSJsonString(requestmap, 2, WappIdCache.WappIdTypeEnum.PICTURE, response);
            }
            //人脸分组
            case ProtocolAnalysisConst.CMDFACE_GROUP: {
                return faceGroupLibToWSJsonString(requestmap, WappIdCache.WappIdTypeEnum.PICTURE, response);
            }
            //人脸分组 1对1
            case ProtocolAnalysisConst.CMDW_TASKGROUPONETOONE: {
                return faceGroupLibToWSJsonStringOne2One(requestmap, WappIdCache.WappIdTypeEnum.PICTURE, response);
            }
            //布控新增
            case ProtocolAnalysisConst.CMDW_TASKADD: {
                return addfaceWTaskToWSJsonString(requestmap, WappIdCache.WappIdTypeEnum.VIDEO, response);
            }
            //布控删除
            case ProtocolAnalysisConst.CMDW_TASKDEL: {
                return deletefaceWTaskToWSJsonString(requestmap, WappIdCache.WappIdTypeEnum.VIDEO, response);
            }
            default:
                return null;
        }
    }

    /**
     * 构造消息公共头部
     *
     * @param headerMap
     */
    public void createPublicWSHeadMap(Map<String, Object> headerMap, WappIdCache.WappIdTypeEnum wappIdTypeEnum) {
        headerMap.put(WSProtocalConst.WAPP_ID, WappIdCache.getAvailableWappId(wappIdTypeEnum.code));
        headerMap.put(WSProtocalConst.SER_ID, SerIdFactory.getSerId());
        headerMap.put(WSProtocalConst.SDK_VERSION, wsProtocalConst.sdkVersion);
        headerMap.put(WSProtocalConst.RET, 0);
        headerMap.put(WSProtocalConst.ERROR_INFO, "");
    }

    /**
     * 创建特征库
     *
     * @param rmp 创建特征库的Map
     * @return 人脸识别请求JSON
     */
    public Map<String, Object> createLibToWSJsonString(Map<String, Object> rmp, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {
        if (checkCreateLibOrDeleteData(rmp, response)) {
            Map<String, Object> dateMap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            Map<String, Object> rs = new HashMap<>(16);
            Map<String, Object> headerMap = new HashMap<>(16);
            headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDLIB_CREATE);
            createPublicWSHeadMap(headerMap, wappIdTypeEnum);
            rs.put(WSProtocalConst.HEADER, headerMap);
            rs.put(WSProtocalConst.LIB_IDS, dateMap.get(ProtocolAnalysisConst.LIBIDS));
            return rs;
        } else {
            return null;
        }
    }

    /**
     * 删除特征库：
     *
     * @param rmp
     * @return
     */
    public Map<String, Object> deleteLibToWSJsonString(Map<String, Object> rmp, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {
        if (checkDataMap(rmp, response)) {
            Map<String, Object> dateMap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            Map<String, Object> headerMap = new HashMap<>(16);
            headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDLIB_DELETE);
            createPublicWSHeadMap(headerMap, wappIdTypeEnum);
            Map<String, Object> rs = new HashMap<>(16);
            rs.put(WSProtocalConst.HEADER, headerMap);
            rs.put(WSProtocalConst.LIB_IDS, dateMap.get(ProtocolAnalysisConst.LIBIDS));
            return rs;
        } else {
            return null;
        }
    }

    /**
     * 人脸信息新增
     *
     * @param rmp
     * @return
     */
    public Map<String, Object> pushFaceinfoLibToWSJsonString(Map<String, Object> rmp, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {

        if (checkpushFaceinfoLib(rmp, response)) {
            Map<String, Object> rs = new HashMap<>(16);
            Map<String, Object> headerMap = new HashMap<>(16);
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDLIB_PUSH);
            createPublicWSHeadMap(headerMap, wappIdTypeEnum);
            Map<String, Object> imgface = new HashMap<>(5);
            Map<String, Object> faceFilt = new HashMap<>(6);
            faceFilt.put(WSProtocalConst.IMG_FCFG_CONFIDENCE, wsProtocalConst.pictureconfidence);
            faceFilt.put(WSProtocalConst.IMG_FCFG_ANGLE, wsProtocalConst.pictureangle);
            faceFilt.put(WSProtocalConst.IMG_FCFG_MINFACESIZE, wsProtocalConst.pictureminFaceSize);
            faceFilt.put(WSProtocalConst.IMG_FCFG_FACEONLYONE, wsProtocalConst.picturefaceOnlyOne);
            imgface.put(WSProtocalConst.IMG_FCFG_FACEFILT, faceFilt);

            rs.put(WSProtocalConst.HEADER, headerMap);
            rs.put(WSProtocalConst.IMG_FCFG, imgface);
            //常量值（这里固定）
            rs.put(WSProtocalConst.IMG_REQTYP, WSProtocalConst.imgReqTyp);

            rs.put(WSProtocalConst.LIB_ID, datemap.get(ProtocolAnalysisConst.FACE_LIBID));

            Map<String, Object> faceimages = new HashMap<>();
            List<String> listimages = (List<String>) datemap.get(ProtocolAnalysisConst.FACE_IMAGES);
            for (String img :
                    listimages) {
                faceimages.put(img, img);
            }

            rs.put(WSProtocalConst.IMG_IMAGS, faceimages);

            return rs;
        } else {
            return null;
        }
    }

    /**
     * 人脸特征删除
     *
     * @param rmp
     * @return
     */
    public Map<String, Object> popFaceInfoLibToWSJsonString(Map<String, Object> rmp, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {
        if (checkPOPFaceinfoLib(rmp, response)) {
            Map<String, Object> rs = new HashMap<>(16);
            Map<String, Object> headerMap = new HashMap<>(16);
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDLIB_POP);
            createPublicWSHeadMap(headerMap, wappIdTypeEnum);
            rs.put(WSProtocalConst.HEADER, headerMap);
            rs.put(WSProtocalConst.LIB_ID, datemap.get(ProtocolAnalysisConst.FACE_LIBID));
            rs.put(WSProtocalConst.FEAT_IDS, datemap.get(ProtocolAnalysisConst.FACE_FACEID));
            return rs;
        } else {
            return null;
        }

    }

    /**
     * 人脸图片搜索：
     *
     * @param rmp
     * @param SerchType 1: 人脸图片搜索，2:人脸图片检测
     * @return
     */
    public Map<String, Object> faceSearchLibToWSJsonString(Map<String, Object> rmp, int SerchType, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {
        if (checkfaceSearchLibinfoLib(rmp, SerchType, response)) {
            Map<String, Object> rs = new HashMap<>(16);
            Map<String, Object> headerMap = new HashMap<>(16);
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDF_SEARCH);
            createPublicWSHeadMap(headerMap, wappIdTypeEnum);
            Map<String, Object> imgface = new HashMap<>(5);
            Map<String, Object> faceFilt = new HashMap<>(6);
            faceFilt.put(WSProtocalConst.IMG_FCFG_CONFIDENCE, wsProtocalConst.pictureconfidence);
            faceFilt.put(WSProtocalConst.IMG_FCFG_ANGLE, wsProtocalConst.pictureangle);
            faceFilt.put(WSProtocalConst.IMG_FCFG_MINFACESIZE, wsProtocalConst.pictureminFaceSize);
            faceFilt.put(WSProtocalConst.IMG_FCFG_FACEONLYONE, wsProtocalConst.picturefaceOnlyOne);
            imgface.put(WSProtocalConst.IMG_FCFG_FACEFILT, faceFilt);
            imgface.put(WSProtocalConst.IMG_GROUPFILT, "");
            //人脸属性检测
            imgface.put(WSProtocalConst.FACE_ATTR,WSProtocalConst.faceAttr);
            if (SerchType == 1) {
                imgface.put(WSProtocalConst.LIB_IDS, datemap.get(ProtocolAnalysisConst.LIBIDS));
                imgface.put(WSProtocalConst.IMG_MINSCORE, datemap.get(ProtocolAnalysisConst.FACE_MINSCORE));
                imgface.put(WSProtocalConst.IMG_MAXRETNB, datemap.get(ProtocolAnalysisConst.FACE_MAXRETNB));
            } else {
                //微云 图片检测LIB_IDS随机一个(使用同一个命令：图像检测，图像搜索）
                List<String> listidsrandom= new ArrayList<>();
                listidsrandom.add(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20));
                imgface.put(WSProtocalConst.LIB_IDS, listidsrandom);
                imgface.put(WSProtocalConst.IMG_MINSCORE, wsProtocalConst.minScore);
                imgface.put(WSProtocalConst.IMG_MAXRETNB, wsProtocalConst.maxRetNb);
            }
            rs.put(WSProtocalConst.HEADER, headerMap);
            rs.put(WSProtocalConst.TASKID_SCFG, imgface); //scfg
            //常量值（这里固定）
            rs.put(WSProtocalConst.IMG_REQTYP, WSProtocalConst.imgReqTyp);
            Map<String ,Object> imagemaplist=new HashMap<>();
            List<String> listOrgimage=(List<String>) datemap.get(ProtocolAnalysisConst.FACE_IMAGES);
            if(listOrgimage!=null&&listOrgimage.size()>0)
            {
                for (String imgstr :listOrgimage) {
                    imagemaplist.put(imgstr,imgstr);
                }
            }
            rs.put(WSProtocalConst.IMG_IMAGS,imagemaplist);

            return rs;
        } else {
            return null;
        }
    }

    /**
     * 人脸分组
     *
     * @param rmp
     * @return
     */
    public Map<String, Object> faceGroupLibToWSJsonString(Map<String, Object> rmp, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {

        if (checkfaceGroupLib(rmp, response)) {
            Map<String, Object> rs = new HashMap<>(16);
            Map<String, Object> headerMap = new HashMap<>(16);
            //数据Datamap
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDFACE_GROUP);
            createPublicWSHeadMap(headerMap, wappIdTypeEnum);
            Map<String, Object> imgdcfg = new HashMap<>(5);
            Map<String, Object> faceFilt = new HashMap<>(6);
            faceFilt.put(WSProtocalConst.IMG_FCFG_CONFIDENCE, wsProtocalConst.pictureconfidence);
            faceFilt.put(WSProtocalConst.IMG_FCFG_ANGLE, wsProtocalConst.pictureangle);
            faceFilt.put(WSProtocalConst.IMG_FCFG_MINFACESIZE, wsProtocalConst.pictureminFaceSize);
            faceFilt.put(WSProtocalConst.IMG_FCFG_FACEONLYONE, wsProtocalConst.picturefaceOnlyOne);
            imgdcfg.put(WSProtocalConst.IMG_FCFG_FACEFILT, faceFilt);
            //分组分值过滤
            imgdcfg.put(WSProtocalConst.IMG_MINSCORE, datemap.get(ProtocolAnalysisConst.FACE_MINSCORE));
            rs.put(WSProtocalConst.HEADER, headerMap);
            rs.put(WSProtocalConst.IMG_DCFG, imgdcfg);
            rs.put(WSProtocalConst.IMG_REQTYP, WSProtocalConst.imgReqTyp);
            List<String> listOrgimage=(List<String>) datemap.get(ProtocolAnalysisConst.FACE_IMAGES);
            Map<String ,Object> imagemaplist=new HashMap<>();
            if(listOrgimage!=null&&listOrgimage.size()>0)
            {
                int index=0;
                for (String imgstr :listOrgimage) {
                    imagemaplist.put(imgstr+"$"+index++,imgstr);
                }
            }
            rs.put(WSProtocalConst.IMG_IMAGS, imagemaplist);
            return rs;
        } else {
            return null;
        }
    }
    /**
     * 人脸分组 1对1
     *
     * @param rmp
     * @return
     */
    public Map<String, Object> faceGroupLibToWSJsonStringOne2One(Map<String, Object> rmp, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {

        if (checkfaceGroupLibOne2One(rmp, response)) {
            Map<String, Object> rs = new HashMap<>(16);
            Map<String, Object> headerMap = new HashMap<>(16);
            //数据Datamap
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDFACE_GROUP);
            createPublicWSHeadMap(headerMap, wappIdTypeEnum);
            Map<String, Object> imgdcfg = new HashMap<>(5);
            Map<String, Object> faceFilt = new HashMap<>(6);
            faceFilt.put(WSProtocalConst.IMG_FCFG_CONFIDENCE, wsProtocalConst.pictureconfidence);
            faceFilt.put(WSProtocalConst.IMG_FCFG_ANGLE, wsProtocalConst.pictureangle);
            faceFilt.put(WSProtocalConst.IMG_FCFG_MINFACESIZE, wsProtocalConst.pictureminFaceSize);
            faceFilt.put(WSProtocalConst.IMG_FCFG_FACEONLYONE, wsProtocalConst.picturefaceOnlyOne);
            imgdcfg.put(WSProtocalConst.IMG_FCFG_FACEFILT, faceFilt);
            //分组分值过滤
            imgdcfg.put(WSProtocalConst.IMG_MINSCORE, datemap.get(ProtocolAnalysisConst.FACE_MINSCORE));
            rs.put(WSProtocalConst.HEADER, headerMap);
            rs.put(WSProtocalConst.IMG_DCFG, imgdcfg);
            rs.put(WSProtocalConst.IMG_REQTYP, WSProtocalConst.imgReqTyp);
            String image1 = (String) datemap.get("image1");
            String image2 = (String) datemap.get("image2");

            Map<String ,Object> imagemaplist=new HashMap<>();
            imagemaplist.put(image1+"$"+1,image1);
            imagemaplist.put(image2+"$"+2,image2);
            rs.put(WSProtocalConst.IMG_IMAGS, imagemaplist);
            return rs;
        } else {
            return null;
        }
    }



    /**
     * 布控新增
     *
     * @param rmp
     * @return
     */
    public Map<String, Object> addfaceWTaskToWSJsonString(Map<String, Object> rmp, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {

            Map<String, Object> rs = new HashMap<>(16);
            Map<String, Object> headerMap = new HashMap<>(16);

            ArrayList <Object> arrayList = (ArrayList <Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            if(arrayList!=null&&arrayList.size()>0) {
                headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDW_TASKADD);
                //构建公共头部
                createPublicWSHeadMap(headerMap, wappIdTypeEnum);
                rs.put(WSProtocalConst.HEADER, headerMap);  //head
                Map<String, Object> cfg = new HashMap<>(5);
                for (Object objarray : arrayList) {

                    Map<String, Object> datemap= (Map<String, Object>)(objarray);
                    Map<String, Object> taskcfg = new HashMap<>(5);
                    Map<String, Object> scfg = new HashMap<>(6);
                    Map<String, Object> faceFilt = new HashMap<>(6);

                    //添加任务CFG
                    cfg.put(datemap.get(ProtocolAnalysisConst.TASKID_CAMERAID).toString(), taskcfg);

                    taskcfg.put(WSProtocalConst.TASKID_PROCID, "CmdVdFSAlarm");
                    taskcfg.put(WSProtocalConst.TASKID_VDURLTYPE, datemap.get(ProtocolAnalysisConst.TASKID_VDTYPE));
                    taskcfg.put(WSProtocalConst.TASKID_VDURL, datemap.get(ProtocolAnalysisConst.TASKID_VDURL));

                    faceFilt.put(WSProtocalConst.IMG_FCFG_CONFIDENCE, wsProtocalConst.vedioconfidence);
                    faceFilt.put(WSProtocalConst.IMG_FCFG_ANGLE, wsProtocalConst.vedioangle);
                    faceFilt.put(WSProtocalConst.IMG_FCFG_MINFACESIZE, wsProtocalConst.vediominFaceSize);
                    faceFilt.put(WSProtocalConst.IMG_FCFG_FACEONLYONE, wsProtocalConst.vediofaceOnlyOne);

                    scfg.put(WSProtocalConst.IMG_FCFG_FACEFILT, faceFilt);
                    scfg.put(WSProtocalConst.LIB_IDS, datemap.get(ProtocolAnalysisConst.LIBIDS));
                    scfg.put(WSProtocalConst.IMG_MINSCORE, datemap.get(ProtocolAnalysisConst.FACE_MINSCORE));
                    scfg.put(WSProtocalConst.IMG_MAXRETNB, datemap.get(ProtocolAnalysisConst.FACE_MAXRETNB));
                    scfg.put(WSProtocalConst.IMG_GROUPFILT, "");
                    //是否开启人脸属性检测
                    scfg.put(WSProtocalConst.FACE_ATTR, WSProtocalConst.faceAttr);
                    //任务属性
                    taskcfg.put(WSProtocalConst.TASKID_SCFG, scfg);
                }
                rs.put(WSProtocalConst.TASK_CFG, cfg); // cfg
                return rs;
            }else
            {
                return null;
            }


    }
    /********************************************参数检测***********************************************/

    /**
     * 删除布控
     *
     * @param rmp
     * @return
     */
    public Map<String, Object> deletefaceWTaskToWSJsonString(Map<String, Object> rmp, WappIdCache.WappIdTypeEnum wappIdTypeEnum, HttpServletResponse response) {
        if (checkDeleteTaskToLib(rmp, response)) {
            Map<String, Object> rs = new HashMap<>(16);
            Map<String, Object> headerMap = new HashMap<>(16);
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            headerMap.put(WSProtocalConst.PORT_ID, WSProtocalConst.CMDW_TASKDEL);
            createPublicWSHeadMap(headerMap, wappIdTypeEnum);
            rs.put(WSProtocalConst.HEADER, headerMap);
            rs.put(WSProtocalConst.TASKID_TASKIDS, datemap.get(ProtocolAnalysisConst.TASKID_LISTS));
            return rs;
        } else {
            return null;
        }
    }

    /**
     * 检测数据格式的Data 参数检测
     *
     */
    private boolean checkDataMap(Map<String, Object> rmp, HttpServletResponse response) {
        try {
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            if (datemap == null) {
                ApiVO apiVO = new ApiVO();
                apiVO.setCode(-1);
                apiVO.setMessage("请求格式不对，不能获取Data数据！");
                SendWarningMsg(apiVO, response);
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 检测创建或删除库参数 参数检测
     */
    private boolean checkCreateLibOrDeleteData(Map<String, Object> rmp, HttpServletResponse response) {

        if (checkDataMap(rmp, response)) {
            Map<String, Object> dateMap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            if (dateMap == null) {
                ApiVO apiVO = new ApiVO();
                apiVO.setCode(-1);
                apiVO.setMessage("创建特征库/删除请求格式不对，不能获取Libs数据！");
                SendWarningMsg(apiVO, response);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 检测人脸特征入库 参数检测
     */
    private boolean checkpushFaceinfoLib(Map<String, Object> rmp, HttpServletResponse response) {
        if (checkDataMap(rmp, response)) {
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            ApiVO apiVO = new ApiVO();
            Object obj = datemap.get(ProtocolAnalysisConst.FACE_LIBID);
            if (obj == null) {
                apiVO.setCode(-1);
                apiVO.setMessage("特征库请求格式不对，不能获取Lib数据！");
                SendWarningMsg(apiVO, response);
                return false;
            }
            try {
                List<String> listimages = (List<String>) datemap.get(ProtocolAnalysisConst.FACE_IMAGES);
                if (listimages.size() == 0) {
                    apiVO.setCode(-1);
                    apiVO.setMessage("特征库请求格式不对，特征入库图片url列表不能为空！");
                    SendWarningMsg(apiVO, response);
                    return false;
                }
            } catch (Exception e) {
                apiVO.setCode(-1);
                apiVO.setMessage("特征库请求格式不对，不能获取入库图片url列表！");
                SendWarningMsg(apiVO, response);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 人脸特征删除 参数检测
     */
    private boolean checkPOPFaceinfoLib(Map<String, Object> rmp, HttpServletResponse response) {
        if (checkDataMap(rmp, response)) {
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            ApiVO apiVO = new ApiVO();
            Object obj = datemap.get(ProtocolAnalysisConst.FACE_LIBID);
            if (obj == null) {
                apiVO.setCode(-1);
                apiVO.setMessage("人脸特征删除请求格式不对，不能获取Lib数据！");
                SendWarningMsg(apiVO, response);
                return false;
            }
            obj = datemap.get(ProtocolAnalysisConst.FACE_FACEID);
            if (obj == null) {
                apiVO.setCode(-1);
                apiVO.setMessage("人脸特征删除请求格式不对，不能获取特征ID数据！");
                SendWarningMsg(apiVO, response);
                return false;
            }

        } else {
            return false;
        }
        return true;
    }

    /**
     * 人脸图片搜索/检测 参数检测
     */
    private boolean checkfaceSearchLibinfoLib(Map<String, Object> rmp, int SerchType, HttpServletResponse response) {
        if (checkDataMap(rmp, response)) {
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            ApiVO apiVO = new ApiVO();
            if (SerchType == 1) {
                Object obj = datemap.get(ProtocolAnalysisConst.LIBIDS);
                if (obj == null) {
                    apiVO.setCode(-1);
                    apiVO.setMessage("人脸图片搜索/检测，不能获取Lib数据！");
                    SendWarningMsg(apiVO, response);
                    return false;
                }
            }
            if (SerchType == 1) {
                try {

                    Double minscore = Double.parseDouble(datemap.get(ProtocolAnalysisConst.FACE_MINSCORE).toString());
                    minscore = Double.parseDouble(datemap.get(ProtocolAnalysisConst.FACE_MAXRETNB).toString());

                } catch (Exception ex) {
                    apiVO.setCode(-1);
                    apiVO.setMessage("人脸图片搜索/检测，不能获取MINSCORE/MAXRETNB数据或格式数据有误！");
                    SendWarningMsg(apiVO, response);
                    return false;
                }
            }
            try {
                List<String> listimages = (List<String>) datemap.get(ProtocolAnalysisConst.FACE_IMAGES);
                if (listimages.size() == 0) {
                    apiVO.setCode(-1);
                    apiVO.setMessage("人脸图片搜索/检测图片url列表不能未空！");
                    SendWarningMsg(apiVO, response);
                    return false;
                }
            } catch (Exception e) {
                apiVO.setCode(-1);
                apiVO.setMessage(" 人脸图片搜索/检测 不能获取入库图片url列表！");
                SendWarningMsg(apiVO, response);
                return false;
            }

        } else {
            return false;
        }
        return true;
    }

    /**
     * 人脸分组 参数检测
     */
    private boolean checkfaceGroupLib(Map<String, Object> rmp, HttpServletResponse response) {
        if (checkDataMap(rmp, response)) {
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            ApiVO apiVO = new ApiVO();
            try {
                List<String> listimages = (List<String>) datemap.get(ProtocolAnalysisConst.FACE_IMAGES);
                if (listimages.size() == 0) {
                    apiVO.setCode(-1);
                    apiVO.setMessage("人脸分组请求格式不对，特征入库图片url列表不能为空！");
                    SendWarningMsg(apiVO, response);
                    return false;
                }
            } catch (Exception e) {
                apiVO.setCode(-1);
                apiVO.setMessage("人脸分组请求格式不对，不能获取入库图片url列表！");
                SendWarningMsg(apiVO, response);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
    /**
     * 人脸分组1V1 参数检测
     */
    private boolean checkfaceGroupLibOne2One(Map<String, Object> rmp, HttpServletResponse response) {
        if (checkDataMap(rmp, response)) {
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            ApiVO apiVO = new ApiVO();
            try {
                 String image1 = (String) datemap.get("image1");
                 String image2 = (String) datemap.get("image2");
                if (StringUtils.isEmpty(image1)||StringUtils.isEmpty(image2)) {
                    apiVO.setCode(-1);
                    apiVO.setMessage("人脸检查1对1请求格式不对，对比图片必须要两张！");
                    SendWarningMsg(apiVO, response);
                    return false;
                }
            } catch (Exception e) {
                apiVO.setCode(-1);
                apiVO.setMessage("人脸检查1对1请求格式不对，不能获取图片列表！");
                SendWarningMsg(apiVO, response);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }


    /**
     * 任务新增 参数检测
     */
    private boolean checkAddTaskToLib(Map<String, Object> rmp, HttpServletResponse response) {

            ArrayList<Object> arrayList = (ArrayList<Object> ) rmp.get(ProtocolAnalysisConst.CMD_DATE);
        for (Object objarray :
                arrayList) {
            Map<String ,Object> datemap=(Map<String, Object>) objarray;
            ApiVO apiVO = new ApiVO();
            Object obj = datemap.get(ProtocolAnalysisConst.TASKID_CAMERAID);
            if (obj == null) {
                apiVO.setCode(-1);
                apiVO.setMessage("任务新增请求格式不对，任务ID不能为空！");
                SendWarningMsg(apiVO, response);
                return false;
            }
            obj = datemap.get(ProtocolAnalysisConst.TASKID_VDTYPE);
            if (obj == null) {
                apiVO.setCode(-1);
                apiVO.setMessage("任务新增请求格式不对，vdUrlType不能为空！");
                SendWarningMsg(apiVO, response);
                return false;
            }
            obj = datemap.get(ProtocolAnalysisConst.TASKID_VDURL);
            if (obj == null) {
                apiVO.setCode(-1);
                apiVO.setMessage("任务新增请求格式不对，vdUrl不能为空！");
                SendWarningMsg(apiVO, response);
                return false;
            }
            try {
                List<String> libids = (List<String>) datemap.get(ProtocolAnalysisConst.LIBIDS);
                if (libids.size() == 0) {
                    apiVO.setCode(-1);
                    apiVO.setMessage("任务新增请求格式不对，库列表不能为空！");
                    SendWarningMsg(apiVO, response);
                    return false;
                }
            } catch (Exception e) {
                apiVO.setCode(-1);
                apiVO.setMessage("任务新增请求格式不对，不能获取库列表！");
                SendWarningMsg(apiVO, response);
                return false;
            }
            try {
                Double doublcore = Double.parseDouble(datemap.get((ProtocolAnalysisConst.FACE_MINSCORE)).toString());
                doublcore = Double.parseDouble(datemap.get((ProtocolAnalysisConst.FACE_MAXRETNB)).toString());
            } catch (Exception e) {
                apiVO.setCode(-1);
                apiVO.setMessage("任务新增请求格式不对，最小分值/每库最大返回结果数据不对！");
                SendWarningMsg(apiVO, response);
                return false;
            }
        }

        return true;
    }

    /**
     * 任务删除 参数检测
     */
    private boolean checkDeleteTaskToLib(Map<String, Object> rmp, HttpServletResponse response) {

        if (checkDataMap(rmp, response)) {
            Map<String, Object> datemap = (Map<String, Object>) rmp.get(ProtocolAnalysisConst.CMD_DATE);
            ApiVO apiVO = new ApiVO();
            try {
                List<String> tasklist = (List<String>) datemap.get(ProtocolAnalysisConst.TASKID_LISTS);
                if (tasklist.size() == 0) {
                    apiVO.setCode(-1);
                    apiVO.setMessage("任务删除 待删除任务列表不能为空！");
                    SendWarningMsg(apiVO, response);
                    return false;
                }
            } catch (Exception e) {
                apiVO.setCode(-1);
                apiVO.setMessage("任务删除格式有误 不能获取到待删除任务列表");
                SendWarningMsg(apiVO, response);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }


}
