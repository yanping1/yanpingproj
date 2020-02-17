package com.dkha.platformasynresultprocess.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dkha.common.entity.vo.facelib.CompareListVO;
import com.dkha.common.entity.vo.position.FaceVO;
import com.dkha.common.entity.vo.position.FeatureVO;
import com.dkha.common.entity.vo.position.PositionVO;
import com.dkha.common.entity.vo.warning.WarningVO;
import com.dkha.common.enums.ApiWarnEnum;
import com.dkha.common.fileupload.MinioUtil;
import com.dkha.common.http.HttpUtil;
import com.dkha.common.redis.RedisUtils;
import com.dkha.common.util.Base64ImageUtils;
import com.dkha.platformasynresultprocess.common.WSProtocalConst;
import com.dkha.platformasynresultprocess.config.RabbitmqConfig;
import com.dkha.platformasynresultprocess.dao.WarningRepository;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @Description:
 * @Title:
 * @author: huangyugang
 * @date: 2019/12/10 15:37
 * @Copyright: 成都电科慧安
 */
@Component
@Slf4j
public class AlarmMsgReceiver {

    @Autowired
    WarningRepository warningRepository;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private HttpUtil httpUtil;
    /**
     * 微云图片链接地址
     */
    @Value("${wy.piclink}")
    private String picklink;
    @Value("${api.server.prefix}")
    private String link;
    /**
     * 摄像头报警存储间隙
     */
    @Value("${camera.interval}")
    private Integer camerainterval;

   @RabbitListener(queues = RabbitmqConfig.ACK_ALARM)
    public void process(String alarmmsg, Channel channel, Message message) throws IOException {
        try {
            if (log.isInfoEnabled()) {
                log.info(RabbitmqConfig.ACK_ALARM + "receive: " + alarmmsg);
            }
            Map<String, Object> rsmap = (Map<String, Object>) JSONObject.parseObject(alarmmsg);
            if (rsmap != null) {
                Map<String, Object> head = null;
                if (rsmap.get("timestamp") != null) {
                    Long timestamp = Long.parseLong(rsmap.get("timestamp").toString());
                    //数据判断
                    rsmap = (Map<String, Object>) rsmap.get("loaddata");
                    head = (Map<String, Object>) rsmap.get("head");
                    String portID = head.get(WSProtocalConst.PORT_ID).toString();
                    if (portID.equalsIgnoreCase(WSProtocalConst.CMDW_VDFSALARM)) {

                        String taskID = rsmap.get(WSProtocalConst.TASK_ID).toString();
                        Object taskidobject = redisTemplate.opsForValue().get(taskID);
                        if (taskidobject != null) {
                            long oldtasktime = Long.parseLong(taskidobject.toString());
                            if (timestamp - oldtasktime <= camerainterval) {
                                if (log.isInfoEnabled()) {
                                    log.info(RabbitmqConfig.ACK_ALARM + "报警信息丢弃: " + timestamp);
                                }
                                // 框架容器，是否开启手动ack按照框架配置
                               channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                                return;
                            }else {
                                redisTemplate.opsForValue().set(taskID, timestamp, 1, TimeUnit.HOURS);
                            }
                        } else {
                            redisTemplate.opsForValue().set(taskID, timestamp, 1, TimeUnit.HOURS);
                        }
                    }
                } else {
                    head = (Map<String, Object>) rsmap.get(WSProtocalConst.HEADER);
                }
                isCheckAlarmIsHitCompare(head, rsmap);
            }
           channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if (log.isDebugEnabled()) {
                log.info(RabbitmqConfig.ACK_ALARM + "receive: " + alarmmsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            if (log.isErrorEnabled()) {
                log.info("ACK_QUEUE_A 接受信息异常{}", e.getMessage());
            }
        }

    }

    /**
     * 判断是否是命中的比对数据
     *
     * @param head
     * @param rsmap
     */
    public void isCheckAlarmIsHitCompare(Map<String, Object> head, Map<String, Object> rsmap) throws Exception {

        String portID = head.get(WSProtocalConst.PORT_ID).toString();
        if (portID.equalsIgnoreCase(WSProtocalConst.CMDW_VDFSALARM)) {
            sendMultiAlarmAndCheck(head, rsmap);
        }
    }

    public void sendMultiAlarmAndCheck(Map<String, Object> head, Map<String, Object> map) throws Exception {

        //拆分为多个报警信息进行发送
        List<WarningVO> listwarning = new ArrayList<>(3);
        String bgImg = ((Map<String, Object>) map.get("urls")).get("img1").toString();

        String newbgImg = bgImg;
        int bgHeight = 0; //图片高度
        int bgwidth = 0;  //图片宽度

        //a. 拆分任务编号和摄像头编号
        String taskId_cameraId = map.get("taskId").toString();
        int index_dash = taskId_cameraId.lastIndexOf("_");
        Integer tasktype=0;//任务类型

        String cameraId = taskId_cameraId;
        String taskId = taskId_cameraId;
        if (index_dash != -1) {
            cameraId = taskId_cameraId.substring(index_dash + 1);
            taskId = taskId_cameraId.substring(0, taskId_cameraId.lastIndexOf("_"));
            if("0".equals(cameraId)){  //如果摄像头ID 是0则为视频解析
                tasktype=1;//视频比对任务
            }
        }

        //1. 拆分特征列表
        // 请求图片外部id对应的人脸id列表(如果图片无人脸则为空)
        Map<String, Object> faceids = (Map<String, Object>) map.get("faceIds");
        // 人脸id对应的人脸位置信息列表(如果图片无人脸则为空)
        Map<String, Object> position = (Map<String, Object>) map.get("position");
        //人脸id对应的特征值搜索结果(如果图片无人脸，或人脸不满足请求消息的过滤参数则为空)
        Map<String, Object> libSchScore = (Map<String, Object>) map.get("libSchScore");

        //重新上传图片到Minia服务器
//        try {
//            BufferedImage bufferedImage = ImageIO.read(new URL(picklink + bgImg));
//            if (null != bufferedImage) {
//                if(libSchScore!=null&&libSchScore.size()>0) {
//                    String suff = bgImg.substring(bgImg.lastIndexOf("."));
//                    InputStream inputStream = Base64ImageUtils.encodeImgage(bufferedImage, suff);
//                    String uuid = UUID.randomUUID().toString().replaceAll("-", "").toString();
//                    JSONObject jsonObject = minioUtil.uploadFile(inputStream, uuid, suff);
//                    if (null != jsonObject && jsonObject.get("flag").equals("0")) {
//                        newbgImg = jsonObject.get("url").toString();
//                    }
//                }
//                //设置图片高度
//                bgHeight = bufferedImage.getHeight();
//                //设置图片宽度
//                bgwidth = bufferedImage.getWidth();
//            }
//            bufferedImage = null;
//        } catch (Exception ex) {
//            log.error("图片转换错误:{}", ex.getMessage());
//            throw ex;
//        }




        //人脸特征属性
        Map<String, Object> faceattribute = (Map<String, Object>) map.get("attribute");
        if (faceids != null && faceids.size() > 0) {
            //循环遍历faceid组合为字符串列表
            for (Map.Entry<String, Object> entry : faceids.entrySet()) {
                Map<String, Object> imgfaceids = (Map<String, Object>) entry.getValue();
                //2. 根据特征码获取头像定位
                // 有可能有多个特征ID
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

                        //人脸信息判断
                        if(faceattribute!=null&&faceattribute.size()>0){
                            Map<String, Object> faceatt = (Map<String, Object>) faceattribute.get(str_faceid);
                            if(faceatt!=null&&faceatt.size()>0){
                                FeatureVO featureVO=new FeatureVO();

                                featureVO.setAge(Integer.parseInt(faceatt.get("age").toString()));
                                featureVO.setGender(Integer.parseInt(faceatt.get("gender").toString())==1?"男":"女");
                                faceVO.setFeature(featureVO);
                            }
                        }

                        if (libSchScore != null && libSchScore.size() > 0) {
                            //3. 根据特征码获取比对分数信息
                            Map<String, Object> facelibSchMap = (Map<String, Object>) libSchScore.get(str_faceid);
                            // 每个库取最大分数的第1个)
                            Map<String, CompareListVO> maxScorePerlib = new HashMap<>(3);

                            if (facelibSchMap != null && facelibSchMap.size() > 0) {
                                List scoreList = (List) facelibSchMap.get("scoreList");
                                if (scoreList != null && scoreList.size() > 0) {

                                    String strlibid = null;
                                    Double strscore = null;
                                    String strfeatid = null;

                                    for (Object obj : scoreList) {
                                        //拆分每个特征和库的分数信息
                                        Map<String, Object> scoreItemMap = (Map<String, Object>) obj;
                                        strlibid = scoreItemMap.get("libId").toString();
                                        strscore = Double.parseDouble(scoreItemMap.get("score").toString());
                                        strfeatid = scoreItemMap.get("featId").toString();
                                        //每个人的每个库中没有包含该分数
                                        if (!maxScorePerlib.containsKey(strlibid)) {
                                            CompareListVO compareListVO = new CompareListVO();
                                            compareListVO.setLibId(strlibid);
                                            compareListVO.setScore(strscore);
                                            compareListVO.setComparisonFaceId(strfeatid);
                                            maxScorePerlib.put(strlibid, compareListVO);

                                        } else {
                                            if (strscore > maxScorePerlib.get(strlibid).getScore()) {
                                                CompareListVO compareListVO = new CompareListVO();
                                                compareListVO.setLibId(strlibid);
                                                compareListVO.setScore(strscore);
                                                compareListVO.setComparisonFaceId(strfeatid);
                                                maxScorePerlib.put(strlibid, compareListVO);
                                            }
                                        }
                                    }
                                }
                            } else {
                                //3.1 没有查询到任何分数的特征信息 就发送空的分数
                                ArrayList<FaceVO> faces = new ArrayList<>();
                                // 构建报警对象 每人，每库，取最高分数
                                WarningVO warningVO = new WarningVO();
                                warningVO.setId(UUID.randomUUID().toString());
                                LocalDateTime ldt = LocalDateTime.now();
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                warningVO.setDate(dtf.format(ldt));
                                warningVO.setTimestamp(ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                                warningVO.setBgImg(newbgImg);
                                warningVO.setBgHeight(bgHeight);
                                warningVO.setBgWidth(bgwidth);
                                warningVO.setIswarning(ApiWarnEnum.MISS.getCode());
                                warningVO.setTaskId(taskId);
                                warningVO.setCameraId(cameraId);
                                warningVO.setTasktype(tasktype);
                                warningVO.setTaskIdCameraId(taskId_cameraId);
                                faceVO.setCompareList(new ArrayList<>(1));
                                faces.add(faceVO);
                                warningVO.setFaces(faces);
                                listwarning.add(warningVO);
                            }
                            if (maxScorePerlib != null && maxScorePerlib.size() > 0) {
                                for (Map.Entry<String, CompareListVO> et : maxScorePerlib.entrySet()) {
                                    List<CompareListVO> compareList = new ArrayList<>();
                                    ArrayList<FaceVO> faces = new ArrayList<>();
                                    // 构建报警对象 每人，每库，取最高分数
                                    WarningVO warningVO = new WarningVO();
                                    warningVO.setId(UUID.randomUUID().toString());
                                    LocalDateTime ldt = LocalDateTime.now();
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    warningVO.setDate(dtf.format(ldt));
                                    warningVO.setTimestamp(ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                                    warningVO.setBgImg(newbgImg);
                                    warningVO.setBgHeight(bgHeight);
                                    warningVO.setBgWidth(bgwidth);
                                    warningVO.setIswarning(ApiWarnEnum.HIT.getCode());
                                    warningVO.setTaskId(taskId);
                                    warningVO.setTasktype(tasktype);
                                    warningVO.setCameraId(cameraId);
                                    warningVO.setTaskIdCameraId(taskId_cameraId);
                                    compareList.add(et.getValue());
                                    faceVO.setCompareList(compareList);
                                    faces.add(faceVO);
                                    warningVO.setFaces(faces);
                                    listwarning.add(warningVO);
                                }
                            }

                        } else {
                            //没有特征信息，发生空的分数
                            ArrayList<FaceVO> faces = new ArrayList<>();
                            // 构建报警对象 每人，每库，取最高分数
                            WarningVO warningVO = new WarningVO();
                            warningVO.setId(UUID.randomUUID().toString());
                            LocalDateTime ldt = LocalDateTime.now();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            warningVO.setDate(dtf.format(ldt));
                            warningVO.setTimestamp(ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                            warningVO.setBgImg(newbgImg);
                            warningVO.setBgHeight(bgHeight);
                            warningVO.setBgWidth(bgwidth);
                            warningVO.setIswarning(ApiWarnEnum.MISS.getCode());
                            warningVO.setTaskId(taskId);
                            warningVO.setTasktype(tasktype);
                            warningVO.setCameraId(cameraId);
                            warningVO.setTaskIdCameraId(taskId_cameraId);
                            faceVO.setCompareList(new ArrayList<>(1));
                            faces.add(faceVO);
                            warningVO.setFaces(faces);
                            listwarning.add(warningVO);
                        }
                    }
                }
            }
        }

        for (WarningVO wvo : listwarning) {
             if(1==wvo.getTasktype()&&(wvo.getIswarning().equals(ApiWarnEnum.MISS.getCode()))){
                 continue;
             }
            warningRepository.save(wvo);
            if (log.isDebugEnabled()) {
                System.out.println("发送API：" + JSON.toJSONString(wvo, SerializerFeature.PrettyFormat));
            }
        }

    }


    /**
     * 任务后台检测、报警结果
     */
    public void sendAlarmAndCheck(Map<String, Object> head, Map<String, Object> map) {

        String bgImg = ((Map<String, Object>) map.get("urls")).get("img1").toString();

        WarningVO warningVO = new WarningVO();
        warningVO.setId(UUID.randomUUID().toString());
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        warningVO.setDate(dtf.format(ldt));
        warningVO.setTimestamp(ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli());

        //重新上传图片到Minia服务器
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(picklink + bgImg));
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
            bufferedImage = null;
        } catch (Exception ex) {
            log.error("图片转换错误:{}", ex.getMessage());
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
        //获取结果集的face信息
        ArrayList<FaceVO> faces = getArrayListFaceVoFromMap(map);

        //设置是否命中搜索
        if (faces != null && faces.size() > 0 && faces.get(0).getCompareList() != null && faces.get(0).getCompareList().size() > 0) {
            warningVO.setIswarning(ApiWarnEnum.HIT.getCode());
        } else {
            warningVO.setIswarning(ApiWarnEnum.MISS.getCode());
        }
        warningVO.setFaces(faces);
        //System.out.println(JSON.toJSONString(warningVO));
        warningRepository.save(warningVO);
        if (log.isDebugEnabled()) {
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
                                        compareListVO.setScore(Double.parseDouble(scoreItemMap.get("score").toString()));
                                        compareListVO.setComparisonFaceId(scoreItemMap.get("featId").toString());
                                        compareList.add(compareListVO);
                                    }
                                }
                            }
                            faceVO.setCompareList(compareList);
                        }
                        faces.add(faceVO);
                    }
                }
            }
        }
        return faces;
    }


}
