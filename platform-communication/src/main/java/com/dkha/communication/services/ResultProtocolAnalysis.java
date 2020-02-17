package com.dkha.communication.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dkha.common.entity.vo.ApiVO;
import com.dkha.common.entity.vo.control.ControlVO;
import com.dkha.common.entity.vo.facelib.CompareListVO;
import com.dkha.common.entity.vo.facelib.FaceDeleteVO;
import com.dkha.common.entity.vo.facelib.FaceLibaryReturnVO;
import com.dkha.common.entity.vo.libary.LibaryVO;
import com.dkha.common.entity.vo.position.*;
import com.dkha.common.entity.vo.warning.WarningVO;
import com.dkha.common.enums.ApiWarnEnum;
import com.dkha.common.fileupload.MinioUtil;
import com.dkha.common.redis.RedisUtils;
import com.dkha.common.util.Base64ImageUtils;
import com.dkha.communication.common.ProtocolAnalysisConst;
import com.dkha.communication.common.WSProtocalConst;
import com.dkha.communication.common.ErrorCodeToMsg;
import com.dkha.communication.dao.WarningRepository;
import com.dkha.communication.httpws.cache.ResponseCache;
import com.dkha.communication.httpws.common.WYRequestResponse;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.StringUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @version V1.0
 * @Description: 返回结果解析：从微云服务返回的结果进行解析
 * @Title:
 * @Package com.dkha.communication.services
 * @author: huangyugang
 * @date: 2019/11/29 11:49
 * @Copyright: 成都电科慧安
 */
@Component
@Slf4j
public class ResultProtocolAnalysis {

    @Autowired
    ResponseCache responseCache;
    @Autowired
    WarningRepository warningRepository;
    @Autowired
    ErrorCodeToMsg wyErrorCodeToMsg;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MinioUtil minioUtil;
    //报警消息队列
    @Autowired
    AlarmMsgProducer alarmMsgProducer;

    @Value("${wy.piclink}")
    private String picklink;

    public void ResultProtocolRresolution(String resultMsg) {
        Map<String, Object> rsmap = (Map<String, Object>) JSONObject.parseObject(resultMsg);
        if (rsmap != null) {
            //数据判断
            Map<String, Object> head = (Map<String, Object>) rsmap.get(WSProtocalConst.HEADER);
            String headtime=head.get(WSProtocalConst.HEAD_TIME).toString();
            String protId = head.get(WSProtocalConst.PORT_ID).toString();
            switch (protId) {
                // 新增特征库
                case WSProtocalConst.CMDLIB_CREATE: {
                    sendResultLibCreate(head, rsmap);
                    return;
                }
                // 删除特征库
                case WSProtocalConst.CMDLIB_DELETE: {
                    sendResultLibDelete(head, rsmap);
                    return;
                }
                //新增人脸特征返回
                case WSProtocalConst.CMDLIB_PUSH: {
                    sendResultLibPush(head, rsmap);
                    return;
                }
                //删除人脸特征返回
                case WSProtocalConst.CMDLIB_POP: {
                    sendResultLibPop(head, rsmap);
                    return;
                }
                //图像搜索/图片检测
                case WSProtocalConst.CMDF_FDETECT:
                case WSProtocalConst.CMDF_SEARCHFACE:
                case WSProtocalConst.CMDF_SEARCH: {
                    sendFaceSearchLib(head, rsmap);
                    return;
                }
                //人脸分组
                case WSProtocalConst.CMDFACE_GROUP: {
                    sendFaceGroup(head, rsmap);
                    return;
                }

                //任务新增
                case WSProtocalConst.CMDW_TASKADD: {
                    sendAddTask(head, rsmap);
                    return;
                }
                //任务删除
                case WSProtocalConst.CMDW_TASKDEL: {
                    sendAddDelete(head, rsmap);
                    return;
                }
                //任务查询
                case WSProtocalConst.CMDW_TASKQUERY: {

                }
                //任务状态变化
                case WSProtocalConst.CMDV_DSTATEUSCHG:{
                    if(checkTaskTateusChg(rsmap)){
                    alarmMsgProducer.ackStatusMQSender(resultMsg,headtime);
                    }
                    return;
                }
                //任务报警
                case WSProtocalConst.CMDW_VDFSALARM: {
                    alarmMsgProducer.ackAlarmMQSender(resultMsg,headtime);
                    return;
                }
                default:
                    return;
            }
        }
    }
    private boolean checkTaskTateusChg( Map<String, Object> rsmap ){

        Map<String, Object> status =( Map<String, Object>) rsmap.get("status");
        if(status!=null){
            for (Map.Entry<String, Object> et:status.entrySet()){
                  String stat=    et.getValue().toString();
                  if("StatusOver".equalsIgnoreCase(stat)){
                      return true;
                  }
                 if("StatusReStatIng".equalsIgnoreCase(stat)){
                    return true;
                }
            }
        }
      return  false;

    }




    private void globalErrorInfoHandler(ApiVO apivo, Map<String, Object> head) {
        int ret = Integer.parseInt(head.get(WSProtocalConst.RET).toString());
        apivo.setCode(ret);
        String errmsg = head.get(WSProtocalConst.ERROR_INFO).toString();
        if (ret == 0 && StringUtil.isBlank(errmsg)) {
            apivo.setMessage("成功");
        } else {
            //2019-12-3 错误码翻译：这里估计以后会变更
            apivo.setMessage(wyErrorCodeToMsg.convertErrorCodeToMsg(head.get(WSProtocalConst.ERROR_INFO).toString()));
        }
    }

    /**
     * 新增特征库
     *
     * @param head 请求头部消息
     * @param map
     */
    private void sendResultLibCreate(Map<String, Object> head, Map<String, Object> map) {
        String serid = head.get(WSProtocalConst.SER_ID).toString();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDLIB_CREATE);
        LibaryVO libaryVO = new LibaryVO();
        List<String> listlib = (List<String>) map.get(WSProtocalConst.LIB_IDS);
        globalErrorInfoHandler(apivo, head);
        libaryVO.setLibIds(listlib);
        apivo.setData(libaryVO);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo));
    }

    /**
     * 删除特征库
     *
     * @param head
     * @param map
     */
    private void sendResultLibDelete(Map<String, Object> head, Map<String, Object> map) {
        String serid = head.get(WSProtocalConst.SER_ID).toString();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDLIB_DELETE);
        LibaryVO libaryVO = new LibaryVO();
        List<String> listlib = (List<String>) map.get(WSProtocalConst.LIB_IDS);
        globalErrorInfoHandler(apivo, head);
        libaryVO.setLibIds(listlib);
        apivo.setData(libaryVO);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo));
    }

    /**
     * 新增人脸特征返回
     */
    private void sendResultLibPush(Map<String, Object> head, Map<String, Object> map) {
        String serid = head.get(WSProtocalConst.SER_ID).toString();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDLIB_PUSH);
        String relibid = (String) map.get(WSProtocalConst.LIB_ID);
        globalErrorInfoHandler(apivo, head);
        FaceLibaryReturnVO faceLibaryReturnVO = new FaceLibaryReturnVO();
        ArrayList<FaceVO> faces = new ArrayList<>();
        // 请求图片外部id对应的人脸id列表(如果图片无人脸则为空)
        Map<String, Object> faceids = (Map<String, Object>) map.get("faceIds");
        // 人脸id对应的人脸位置信息列表(如果图片无人脸则为空)
        Map<String, Object> position = (Map<String, Object>) map.get("position");
        if (faceids != null) {
            //循环遍历faceid组合为字符串列表
            for (Map.Entry<String, Object> entry : faceids.entrySet()) {
                Map<String, Object> imgfaceids = (Map<String, Object>) entry.getValue();
                //有可能有多个特征ID
                List<Object> listfaceids_faceid = (List<Object>) imgfaceids.get("faceIds");
                for (Object str_faceid : listfaceids_faceid) {
                    FaceVO faceVO = new FaceVO();
                    faceVO.setBgImg(entry.getKey());
                    faceVO.setFaceId(str_faceid.toString());
                    if (position != null) {
                        Map<String, Object> faceMap = (Map<String, Object>) position.get(str_faceid);
                        Map<String, Object> faceRectMap = (Map<String, Object>) faceMap.get("rect");
                        PositionVO positionVO = new PositionVO();
                        Integer left = Integer.parseInt(faceRectMap.get("left").toString());
                        Integer top = Integer.parseInt(faceRectMap.get("top").toString());
                        Integer right = Integer.parseInt(faceRectMap.get("right").toString());
                        Integer bottom = Integer.parseInt(faceRectMap.get("bottom").toString());
                        positionVO.setX(left);
                        positionVO.setY(top);
                        positionVO.setW(right - left);
                        positionVO.setH(bottom - top);
                        faceVO.setPosition(positionVO);
                    }
                    faces.add(faceVO);
                }
            }
        }
        faceLibaryReturnVO.setFaces(faces);
        faceLibaryReturnVO.setLibId(relibid);
        apivo.setData(faceLibaryReturnVO);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo, SerializerFeature.PrettyFormat));
    }

    /**
     * 人像入库-删除
     */
    private void sendResultLibPop(Map<String, Object> head, Map<String, Object> map) {

        String serid = head.get(WSProtocalConst.SER_ID).toString();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDLIB_POP);
        FaceDeleteVO faceDeleteVO = new FaceDeleteVO();
        String libid = map.get(WSProtocalConst.LIB_ID).toString();
        List<String> faceids = (List<String>) map.get(WSProtocalConst.FEAT_IDS);
        globalErrorInfoHandler(apivo, head);
        faceDeleteVO.setLibId(libid);
        faceDeleteVO.setFaceIds(faceids);
        apivo.setData(faceDeleteVO);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo));
    }

    /**
     * 人脸信息搜索结果
     */
    private void sendFaceSearchLib(Map<String, Object> head, Map<String, Object> map) {

        String serid = head.get(WSProtocalConst.SER_ID).toString();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDF_SEARCH);
        String relibid = "";
        Set<String> setlibid = new HashSet<>();
        globalErrorInfoHandler(apivo, head);

        FaceLibaryReturnVO faceLibaryReturnVO = new FaceLibaryReturnVO();
        ArrayList<FaceVO> faces = getArrayListFaceVoFromMap(map);
        faceLibaryReturnVO.setFaces(faces);
        faceLibaryReturnVO.setLibId("");
        apivo.setData(faceLibaryReturnVO);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo, SerializerFeature.PrettyFormat));

    }

    /**
     * 人脸分组
     */
    private void sendFaceGroup(Map<String, Object> head, Map<String, Object> map) {
        String serid = head.get(WSProtocalConst.SER_ID).toString();
        GroupVO groupVO = new GroupVO();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDFACE_GROUP);
        globalErrorInfoHandler(apivo, head);
        Map<String, Object> positon = (Map<String, Object>) map.get("position");
        List listscoreMap = (List) map.get("fScoreComp");

        if (positon != null && positon.size() > 0) {
            List<GroupPositionVO> faceIdsmap = new ArrayList<>();
            for (Map.Entry<String, Object> entry : positon.entrySet()) {
                GroupPositionVO gpvo = new GroupPositionVO();
                Map<String, Object> rectmap = (Map<String, Object>) ((Map<String, Object>) entry.getValue()).get("rect");
                gpvo.setFaceId(entry.getKey());
                Integer left = Integer.parseInt(rectmap.get("left").toString());
                Integer top = Integer.parseInt(rectmap.get("top").toString());
                Integer right = Integer.parseInt(rectmap.get("right").toString());
                Integer bottom = Integer.parseInt(rectmap.get("bottom").toString());
                gpvo.setX(left);
                gpvo.setY(right);
                gpvo.setW(right - left);
                gpvo.setH(bottom - top);
                gpvo.setFaceId(entry.getKey());
                String imglabel = ((Map<String, Object>) entry.getValue()).get("extId").toString();
                String[] imgls = imglabel.split("\\$");
                if (imgls != null && imgls.length > 0) {
                    gpvo.setBgUrl(imgls[0]);
                } else {
                    gpvo.setBgUrl(imglabel);
                }

                faceIdsmap.add(gpvo);
            }
            groupVO.setFaceIds(faceIdsmap);
        }
        if (listscoreMap != null) {
            List<FScoreCompVO> listscore = new ArrayList<>();

            for (Object scoreObj : listscoreMap) {
                FScoreCompVO fScoreCompVO = new FScoreCompVO();
                Map<String, Object> scoreMap = (Map<String, Object>) scoreObj;
                fScoreCompVO.setFaceId(scoreMap.get("feat1").toString());
                fScoreCompVO.setComparisonFaceId(scoreMap.get("feat2").toString());
                fScoreCompVO.setScore(Double.parseDouble(scoreMap.get("score").toString()));
                listscore.add(fScoreCompVO);
            }

            groupVO.setFScoreComp(listscore);
        }

        Map<String, Object> faceIds = (Map<String, Object>) map.get("faceIds");
        if (faceIds != null && faceIds.size() > 0) {
            Map<String, Imginfo> faceidmap = new HashMap<>();

            for (Map.Entry<String, Object> entry : faceIds.entrySet()) {
                Imginfo imginfo = new Imginfo();
                imginfo.setFaceIds((List) ((Map<String, Object>) entry.getValue()).get("faceIds"));
                imginfo.setReSizeH(Integer.parseInt(((Map<String, Object>) entry.getValue()).get("reSizeH").toString()));
                imginfo.setReSizeW(Integer.parseInt(((Map<String, Object>) entry.getValue()).get("reSizeW").toString()));
                faceidmap.put(entry.getKey(), imginfo);

            }

            groupVO.setFaceId(faceidmap);
        }
        apivo.setData(groupVO);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo, SerializerFeature.PrettyFormat));
    }

    /**
     * 任务新增
     */
    private void sendAddTask(Map<String, Object> head, Map<String, Object> map) {
        String serid = head.get(WSProtocalConst.SER_ID).toString();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDW_TASKADD);
        globalErrorInfoHandler(apivo, head);

        List<String> listlib = (List<String>) map.get(WSProtocalConst.TASKID_TASKIDS);
        ControlVO controlVO = new ControlVO();
        controlVO.setTaskIdCameraIds(listlib);
        apivo.setData(listlib);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo));

    }

    /**
     * 任务删除
     */
    private void sendAddDelete(Map<String, Object> head, Map<String, Object> map) {
        String serid = head.get(WSProtocalConst.SER_ID).toString();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDW_TASKDEL);
        globalErrorInfoHandler(apivo, head);

        List<String> listlib = (List<String>) map.get(WSProtocalConst.TASKID_TASKIDS);
        ControlVO controlVO = new ControlVO();
        controlVO.setTaskIdCameraIds(listlib);
        apivo.setData(listlib);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo));
    }

    /**
     * 任务查询
     */
    private void sendAddQuery(Map<String, Object> head, Map<String, Object> map) {
        String serid = head.get(WSProtocalConst.SER_ID).toString();
        ApiVO apivo = new ApiVO();
        apivo.setCmd(ProtocolAnalysisConst.CMDW_TASKQUERY);
        globalErrorInfoHandler(apivo, head);

        List<String> listlib = (List<String>) map.get(WSProtocalConst.TASKID_TASKIDS);
        ControlVO controlVO = new ControlVO();
        controlVO.setTaskIdCameraIds(listlib);
        apivo.setData(listlib);
        SendShortConnectionMSGToCustomer(serid, JSON.toJSONString(apivo));
    }

    /**
     * 任务后台检测、报警结果
     */
    public void sendAlarmAndCheck(Map<String, Object> head, Map<String, Object> map)  {
        String bgImg = ((Map<String, Object>) map.get("urls")).get("img1").toString();
        WarningVO warningVO = new WarningVO();
        warningVO.setId(UUID.randomUUID().toString());
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        warningVO.setDate(dtf.format(ldt));
        warningVO.setTimestamp(ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli());

        //重新上传图片到Minia服务器
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(  picklink+ bgImg));
            if (null != bufferedImage) {
                String suff = bgImg.substring(bgImg.lastIndexOf("."));
                InputStream inputStream = Base64ImageUtils.encodeImgage(bufferedImage, suff);
                String uuid = UUID.randomUUID().toString().replaceAll("-", "").toString();
                JSONObject jsonObject = minioUtil.uploadFile(inputStream, uuid, suff);
                if (null != jsonObject && jsonObject.get("flag").equals("0")) {
                    warningVO.setBgImg(jsonObject.get("url").toString());
                }
                //设置图片高度
                warningVO.setBgHeight(bufferedImage.getHeight());
                //设置图片宽度
                warningVO.setBgWidth(bufferedImage.getWidth());

            }
            bufferedImage=null;

        }catch (Exception ex){
           log.error("图片转换错误:{}",ex.getMessage());
        }

        String taskId_cameraId = map.get("taskId").toString();
        int index_dash = taskId_cameraId.lastIndexOf("_");

        String cameraId = taskId_cameraId;
        String taskId = taskId_cameraId;
        if (index_dash != -1) {
            cameraId = taskId_cameraId.substring(index_dash + 1);
            taskId = taskId_cameraId.substring(0, taskId_cameraId.lastIndexOf("_"));
        }
        warningVO.setTaskIdCameraId(taskId_cameraId);
        warningVO.setTaskId(taskId);
        warningVO.setCameraId(cameraId);
        ArrayList<FaceVO> faces = getArrayListFaceVoFromMap(map);
        //设置是否命中搜索
        if(faces!=null&&faces.size()>0&&faces.get(0).getCompareList().size()>0){
            warningVO.setIswarning(ApiWarnEnum.HIT.getCode());
        }else{
            warningVO.setIswarning(ApiWarnEnum.MISS.getCode());
        }
        warningVO.setFaces(faces);
        WarningVO save = warningRepository.save(warningVO);
        if(log.isDebugEnabled()){
            System.out.println("发送API：" + JSON.toJSONString(warningVO, SerializerFeature.PrettyFormat));
        }
    }

    /**
     * 获取结果集中的face信息
     */
    private ArrayList<FaceVO> getArrayListFaceVoFromMap(Map<String, Object> map) {
        ArrayList<FaceVO> faces = new ArrayList<>();
        // 请求图片外部id对应的人脸id列表(如果图片无人脸则为空)
        Map<String, Object> faceids = (Map<String, Object>) map.get("faceIds");
        // 人脸id对应的人脸位置信息列表(如果图片无人脸则为空)
        Map<String, Object> position = (Map<String, Object>) map.get("position");
        //人脸id对应的特征值搜索结果(如果图片无人脸，或人脸不满足请求消息的过滤参数则为空)
        Map<String, Object> libSchScore = (Map<String, Object>) map.get("libSchScore");
        //人脸特征属性
        Map<String, Object> faceattribute = (Map<String, Object>) map.get("attribute");

        if (faceids != null && faceids.size() > 0) {
            //循环遍历faceid组合为字符串列表
            for (Map.Entry<String, Object> entry : faceids.entrySet()) {
                Map<String, Object> imgfaceids = (Map<String, Object>) entry.getValue();
                //有可能有多个特征ID
                List<Object> listfaceids_faceid = (List<Object>) imgfaceids.get("faceIds");
                if (listfaceids_faceid != null && listfaceids_faceid.size() > 0) {
                    for (Object str_faceid : listfaceids_faceid) {

                        FaceVO faceVO = new FaceVO();
                        faceVO.setBgImg(entry.getKey());
                        faceVO.setFaceId(str_faceid.toString());
                        if (position != null && position.size() > 0) {
                            Map<String, Object> faceMap = (Map<String, Object>) position.get(str_faceid);
                            Map<String, Object> faceRectMap = (Map<String, Object>) faceMap.get("rect");
                            PositionVO positionVO = new PositionVO();
                            Integer left = Integer.parseInt(faceRectMap.get("left").toString());
                            Integer top = Integer.parseInt(faceRectMap.get("top").toString());
                            Integer right = Integer.parseInt(faceRectMap.get("right").toString());
                            Integer bottom = Integer.parseInt(faceRectMap.get("bottom").toString());
                            positionVO.setX(left);
                            positionVO.setY(top);
                            positionVO.setW(right - left);
                            positionVO.setH(bottom - top);
                            faceVO.setPosition(positionVO);
                        }
                        //人脸库比对
                        if (libSchScore != null && libSchScore.size() > 0) {
                            List<CompareListVO> compareList = new ArrayList<>();
                            Map<String, Object> facelibSchMap = (Map<String, Object>) libSchScore.get(str_faceid);
                            if (facelibSchMap != null && facelibSchMap.size() > 0) {
                                List scoreList = (List) facelibSchMap.get("scoreList");
                                if (scoreList != null && scoreList.size() > 0) {
                                    for (Object obj : scoreList) {
                                        Map<String, Object> scoreItemMap = (Map<String, Object>) obj;
                                        CompareListVO compareListVO = new CompareListVO();
                                        compareListVO.setLibId(scoreItemMap.get("libId").toString());
                                        // setlibid.add(compareListVO.getLibId());
                                        compareListVO.setScore(Double.parseDouble(scoreItemMap.get("score").toString()));
                                        compareListVO.setComparisonFaceId(scoreItemMap.get("featId").toString());
                                        compareList.add(compareListVO);
                                    }
                                }
                            }
                            faceVO.setCompareList(compareList);
                        }
                        //人脸信息判断
                        if(faceattribute!=null&&faceattribute.size()>0){
                            Map<String, Object> faceatt = (Map<String, Object>) faceattribute.get(str_faceid);
                            if(faceatt!=null&&faceatt.size()>0){
                                FeatureVO featureVO=new FeatureVO(Integer.parseInt(faceatt.get("age").toString()),Integer.parseInt(faceatt.get("gender").toString())==1?"男":"女");
                                faceVO.setFeature(featureVO);
                            }
                        }
                        faces.add(faceVO);
                    }
                }
            }
        }
        return faces;
    }


    /**
     * 发送消息给客户端
     */
    public void SendShortConnectionMSGToCustomer(String serid, String msg) {
        if (responseCache != null) {
            WYRequestResponse response = responseCache.get(serid);
            if (response != null) {
                HttpServletResponse httpServletResponse = response.getResponse();
                if (httpServletResponse != null) {
                    try {
                        httpServletResponse.getWriter().print(msg);
                        httpServletResponse.getWriter().flush();

                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    } finally {
                        response.getCountDownLatch().countDown();
                        httpServletResponse = null;
                        response = null;
                    }
                }
            } else {
                if(log.isDebugEnabled()){
                    log.debug("通过{}查找Respongs[{ }]为空",serid,msg);
                }

            }
        }
    }

}
