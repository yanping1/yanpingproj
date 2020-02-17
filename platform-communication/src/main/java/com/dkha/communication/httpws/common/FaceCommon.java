package com.dkha.communication.httpws.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dkha.common.util.IntUUID;
import com.dkha.common.validate.UtilValidate;
import com.dkha.communication.httpws.cache.WappIdCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Author Spring
 * @Since 2019/8/14 11:56
 * @Description 人脸公共类
 */
@Component
@PropertySource({"classpath:system.properties"})
public class FaceCommon {

    public static final Logger logger = LoggerFactory.getLogger(FaceCommon.class);
    /** 控制命令字段*/
    public static final String PORT_ID = "protId";
    /** wappId 常量 */
    public static final String WAPP_ID = "wappId";
    /** sdk 版本 */
    public static final String SDK_VERSION = "version";

    /** 图片请求类型 */
    public static final String IMG_REQTYP = "imgReqTyp";

    /** serId常亮 */
    public static final String SER_ID = "serId";
    /** 协议头静态常量 */
    public static final String HEADER = "head";
    /** 结果码 0为正确 */
    public static final String RET = "ret";
    /** 错误信息 */
    public static final String ERROR_INFO = "errInfo";
    /** 特征库集合 */
    public static final String LIB_IDS = "libIds";
    /** 人脸id对应的人脸位置信息列表(如果图片无人脸则为空) */
    public static final String POSITION = "position";
    /** 人脸特征Id集合 */
    public static final String FEAT_IDS = "featIds";
    /** 人脸特征Id */
    public static final String FEAT_ID = "featId";
    /** 库搜索结果 */
    public static final String LIB_SCH_SCORE = "libSchScore";
    /** 库Id */
    public static final String LIB_ID = "libId";
    /** scoreList */
    public static final String SCORE_LIST = "scoreList";
    /** 评分 */
    public static final String SCORE = "score";
    /** 人脸rect */
    public static final String RECT = "rect";

    public static final String EXT_ID = "extId";
    /** face sdk 版本 */
    public static String sdkVersion;
    /** 人脸置信度 <0不启用*/
    public static float confidence;
    /** 人脸角度过滤 <0不启用 */
    public static float angle;
    /** 最小人脸过滤, 单边人脸边长像素 */
    public static int minFaceSize;
    /** 只选取图片最大人脸 */
    public static boolean faceOnlyOne;
    /** 图片请求类型 */
    public static String imgReqTyp;
    public static String YES="Y";
    public static String NO="N";

    @Value("ReqImgHttpUrl")
    public void setImgReqTyp(String imgReqTyp) {
        FaceCommon.imgReqTyp = imgReqTyp;
    }

    @Value("${weiyun.version}")
    public void setSdkVersion(String sdkVersion) {
        FaceCommon.sdkVersion = sdkVersion;
    }
    @Value("${confidence}")
    public void setConfidence(float confidence) {
        FaceCommon.confidence = confidence;
    }
    @Value("${angle}")
    public void setAngle(float angle) {
        FaceCommon.angle = angle;
    }
    @Value("${minFaceSize}")
    public void setMinFaceSize(int minFaceSize) {
        FaceCommon.minFaceSize = minFaceSize;
    }
    @Value("${faceOnlyOne}")
    public void setFaceOnlyOne(boolean faceOnlyOne) {
        FaceCommon.faceOnlyOne = faceOnlyOne;
    }

    /**
     * 获取公共请求头
     * @param command face sdk 控制命令字段
     * @param wappIdType
     * @return
     */
    public static Map<String, Object> getRequestHeader(String command, String wappIdType) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("protId", command);
        //如果网络初始化未完成，则当前线程休眠，直到网络连接完成
        boolean flag = true;
        while (flag) {
            //默认获取图片相关的
            if (!(UtilValidate.isEmpty(WappIdCache.getAvailableWappId(wappIdType)) || WappIdCache.getAvailableWappId(wappIdType) == 0)) {
                flag = false;
            } else {
                try {
                    //网络初始化未完成，当前线程休眠500ms
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        headerMap.put("wappId", WappIdCache.getAvailableWappId(wappIdType));
        headerMap.put("serId", IntUUID.getShortUUID());
        headerMap.put("version", sdkVersion);
        //结果/错误码 默认/正确为0
        headerMap.put("ret", 0);
        //错误信息
        headerMap.put("errInfo", "");
        return headerMap;
    }

    /**
     * 获取只有请求头的请求Map
     * @param command
     * @return
     */
    public static Map<String, Object> getRequestHeaderMap(String command, String wappIdType) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("head", getRequestHeader(command, wappIdType));
        return requestMap;
    }

    /**
     * 获取公共请求头
     * @param command face sdk 控制命令字段
     * @return
     */
    public static String getRequestHeaderMsg(String command, String wappIdType) {
         String headerStr = JSON.toJSONString(getRequestHeader(command, wappIdType), SerializerFeature.WriteNullStringAsEmpty);
        return headerStr;
    }

    /**
     * 获取 人脸检测过滤配置
     * @return
     */
    public static Map<String, Object> getRequestFaceFilt() {
        Map<String, Object> faceFiltMap = new HashMap<>(4);
        faceFiltMap.put("confidence", confidence);
        faceFiltMap.put("angle", angle);
        faceFiltMap.put("minFaceSize", minFaceSize);
        faceFiltMap.put("faceOnlyOne", faceOnlyOne);
        return faceFiltMap;
    }

    /**
     * 获取SCFG MAP
     * @param faceFilt 图片人脸检测过滤
     * @param groupFilt 人脸分组过滤
     * @param libIds 搜索特征库列表
     * @param minScore 最小分值 取值范围 0.00-1.00
     * @param maxRetNb 每个库的最大返回结果数 0-100
     * @return
     */
    public static Map<String, Object> getRequestSCFGSearch(Map<String, Object> faceFilt, String groupFilt, List<String> libIds, Double minScore, Integer maxRetNb) {
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("faceFilt", faceFilt);
        resultMap.put("groupFilt", groupFilt);
        resultMap.put("libIds", libIds);
        resultMap.put("minScore", minScore);
        resultMap.put("maxRetNb", maxRetNb);
        return resultMap;
    }

    /**
     * 获取FCFG MAP
     * @param faceFilt
     * @return
     */
    public static Map<String, Object> getRequestFCFGSearch(Map<String, Object> faceFilt) {
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("faceFilt", faceFilt);
        return resultMap;
    }

    /**
     * 获取SCFG公共请求体
     * @param command 当前请求接口命令
     * @param groupFilt 人脸搜索结构定义-图片人脸检测过滤
     * @param libIdsFilt 人脸搜索结构定义-人脸分组过滤
     * @param minScoreFilt 人脸搜索结构定义-搜索特征库列表
     * @param maxRetNbFilt 人脸搜索结构定义-每个库的最大返回结果数 0-100
     * @param imgReqType 图片请求类型
     * @param imgs 图片外部Id对应的图片地址列表/图片列表
     * @param libId 需要操作的特征库Id
     * @return
     */
    public static Map<String, Object> getCommonRequestSCFGMap(String command, String groupFilt, List<String> libIdsFilt, Double minScoreFilt, Integer maxRetNbFilt, String imgReqType,  List<String> imgs, String libId, String wappIdType) {
        Map<String, Object> commonRequestMap = new HashMap<>(16);
        //请求头
        commonRequestMap.put("head", getRequestHeader(command, wappIdType));
        //人脸搜索结构定义
        commonRequestMap.put("scfg", getRequestSCFGSearch(getRequestFaceFilt(), groupFilt, libIdsFilt, minScoreFilt, maxRetNbFilt));
        //图片请求类型
        commonRequestMap.put("imgReqTyp", imgReqType);
        //当前需要操作的库
        if (UtilValidate.isNotEmpty(libId)) {
            commonRequestMap.put("libId", libId);
        }
        /**
         * 当前请求对应的图片列表
         */
        if (UtilValidate.isNotEmpty(imgs)) {
            Map<String, String> imageMap = new HashMap<>();
            for (String imageUrl : imgs) {
                //使用图片地址作为key，以达到唯一性，以满足数据关联
                imageMap.put(imageUrl, imageUrl);
            }
            commonRequestMap.put("imgs", imageMap);
        }
        return commonRequestMap;
    }

    /**
     * 获取FCFG公共请求体
     * @param command 当前请求接口命令
     * @param imgReqType 图片请求类型
     * @param imgs 图片外部Id对应的图片地址列表/图片列表
     * @param libId 需要操作的特征库Id
     * @return
     */
    public static Map<String, Object> getCommonRequestFCFGMap(String command, String imgReqType, List<String> imgs, String libId, String wappIdType) {
        Map<String, Object> commonRequestMap = new HashMap<>(16);
        //请求头
        commonRequestMap.put("head", getRequestHeader(command, wappIdType));
        //人脸搜索结构定义
        commonRequestMap.put("fcfg", getRequestFCFGSearch(getRequestFaceFilt()));
        //图片请求类型
        commonRequestMap.put("imgReqTyp", imgReqType);
        //当前需要操作的库
        if (UtilValidate.isNotEmpty(libId)) {
            commonRequestMap.put("libId", libId);
        }
        /**
         * 当前请求对应的图片列表
         */
        if (UtilValidate.isNotEmpty(imgs)) {
            Map<String, String> imageMap = new HashMap<>();
            for (String imageUrl : imgs) {
                //使用图片地址作为key，以达到唯一性，以满足数据关联
                imageMap.put(imageUrl, imageUrl);
            }
            commonRequestMap.put("imgs", imageMap);
        }
        return commonRequestMap;
    }

    /**
     * 获取 将人脸从特征库删除 请求头
     * @param command
     * @param libId
     * @param featIds
     * @return
     */
    public static Map<String, Object> getDeleteFromFactoryRequestMap(String command, String libId, List<String> featIds, String wappIdType) {
        Map<String, Object> requestHeaderMap = getRequestHeader(command, wappIdType);
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("head", requestHeaderMap);
        requestMap.put("libId", libId);
        requestMap.put("featIds", featIds);
        return requestMap;
    }

    /**
     * 获取公共请求头及其搜索结构Map
     * @param headMap 头部信息
     * @param scfgMap 过滤参数Map
     * @return
     */
    public static Map<String, Object> getHeadAndScfg(Map<String, Object> headMap, Map<String, Object> scfgMap) {
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("head", headMap);
        if (UtilValidate.isNotEmpty(scfgMap)) {
            resultMap.put("scfg", scfgMap);
        }
        return resultMap;
    }

    /**
     * 从response中获取错误码及其错误信息
     * @param response
     * @return 错误代码key为 ret
     *          错误信息key为errorInfo
     */
    public static Map<String, String> getErrorMsgMapFromResponse(String response) {
        if (UtilValidate.isEmpty(response)) {
            return null;
        }
        Map<String, Map<String, Object>> responseMap = JSON.parseObject(response, Map.class);
        Map<String, String> errorMap = new HashMap<>();
        if (Integer.parseInt(responseMap.get(HEADER).get(RET).toString()) != 0) {
            errorMap.put(RET, responseMap.get(HEADER).get(RET) + "");
            errorMap.put(ERROR_INFO, responseMap.get(HEADER).get(ERROR_INFO) + "");
        }
        return errorMap;
    }

    /**
     * 判断返回结果集里面是否包含错误信息
     * @param response
     * @return
     */
    public static boolean isExistErrorMsgFromResponse(String response) {
        if (UtilValidate.isNotEmpty(getErrorMsgMapFromResponse(response))) {
            return true;
        }
        return false;
    }

    /**
     * 根据指定的key在返回集合中获取数据
     * @param response
     * @param key
     * @return
     */
    public static Object getResultDataByKey(String response, String key) {
        Map<String, Map<String, Object>> responseMap = JSON.parseObject(response, Map.class);
        return responseMap.get(key);
    }

    /**
     * 从header返回json里面获取wappid
     * @param headerJson
     * @return
     */
    public static int getWappIdFromHeads(String headerJson) {
        Map<String, Map<String, Object>> headerMap = JSON.parseObject(headerJson, Map.class);
        return (Integer) headerMap.get(HEADER).get(WAPP_ID);
    }
    /**
     * 从header返回json里面获取serId
     * @param headerJson
     * @return
     */
    public static int getSerIdFromHeaders(String headerJson) {
        Map<String, Map<String, Object>> headerMap = JSON.parseObject(headerJson, Map.class);
        return Integer.parseInt(headerMap.get(HEADER).get(SER_ID).toString());
    }

    public static void main(String[] args) {
        System.out.println(FaceCommon.getRequestHeader("command", WappIdCache.WappIdTypeEnum.PICTURE.code));
    }
}
