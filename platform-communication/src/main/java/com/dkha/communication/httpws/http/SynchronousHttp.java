package com.dkha.communication.httpws.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dkha.common.exception.DkException;
import com.dkha.common.result.systemcode.SystemCode;
import com.dkha.common.validate.UtilValidate;
import com.dkha.communication.httpws.cache.ChannelCache;
import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.httpws.cache.WsResultCache;
import com.dkha.communication.httpws.common.FaceCommon;
import com.dkha.communication.httpws.factory.SerIdFactory;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @Author Spring
 * @Since 2019/8/16 11:57
 * @Description 同步http请求
 */
@Component
@PropertySource({"classpath:system.properties"})
public class SynchronousHttp {

    public static final Logger logger = LoggerFactory.getLogger(SynchronousHttp.class);

    /**
     * 系统默认http请求超时时间
     */
    @Value("${http.timeout}")
    private long httpTimeOut;
    /**
     * 线程休眠时间
     */
    @Value("${sleep.time}")
    private long sleepTime;

    @Autowired
    private WsResultCache wsResultCache;

    /**
     * 创建图片相关同步http请求
     * @param requestMap 请求参数Map
     * @return serId
     */
    public int createPictureSynchronousHttp(Map<String, Object> requestMap) {
        //请求json
        String requestStr = JSON.toJSONString(this.setWappIdAndSerId(WappIdCache.WappIdTypeEnum.PICTURE.code, requestMap), SerializerFeature.WriteNullStringAsEmpty);

        logger.info("图片 客户端请求消息: {}", requestStr);
        //生成serId，并将当前当前请求放置于缓存中
        int serId = FaceCommon.getSerIdFromHeaders(requestStr);
        //请求face 3.2.0
        ChannelCache.get(WappIdCache.WappIdTypeEnum.PICTURE.code).writeAndFlush(new TextWebSocketFrame(requestStr));
        return serId;
    }

    /**
     * 创建视频相关同步http请求
     * @param requestMap 请求参数Map
     * @return serId
     */
    public int createVideoSynchronousHttp(Map<String, Object> requestMap) {
        //请求json
        String requestStr = JSON.toJSONString(this.setWappIdAndSerId(WappIdCache.WappIdTypeEnum.VIDEO.code, requestMap), SerializerFeature.WriteNullStringAsEmpty);

        logger.info("视频 客户端请求消息: {}", requestStr);
        //生成serId，并将当前当前请求放置于缓存中
        int serId = FaceCommon.getSerIdFromHeaders(requestStr);
        //请求face 3.2.0
        ChannelCache.get(WappIdCache.WappIdTypeEnum.VIDEO.code).writeAndFlush(new TextWebSocketFrame(requestStr));
        return serId;
    }

    /**
     * 获取返回结果
     * @param serId
     * @return
     */
    public Map<String, Object> getResponse(int serId) {
        String response = null;
        long startTime = System.currentTimeMillis();
        //循环标志位
        boolean flag = true;
        while (flag) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            response = wsResultCache.get(serId + "");
            if (UtilValidate.isNotEmpty(response)) {
                flag = false;
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > httpTimeOut) {
                flag = false;
            }
        }
        if (UtilValidate.isEmpty(response)) {
            throw new DkException(SystemCode.SYNC_REQUEST_NOT_RESPONSE.code);
        }
        JSONObject  jsonObject = JSONObject.parseObject(response);
        return ((Map<String, Object>) jsonObject);
    }

    /**
     * 请求数据中设置wappId
     * @param wappIdType
     * @param data
     * @return
     */
    private Map<String, Object> setWappIdAndSerId(String wappIdType, Map<String, Object> data) {
        Map<String, Object> headMap = (Map<String, Object>)data.get("head");
        //设置wappId serId
        headMap.put("wappId", WappIdCache.getAvailableWappId(wappIdType));
        headMap.put("serId", SerIdFactory.getSerId());
        headMap.put("version", "3.2.0");
        headMap.put("ret", 0);
        headMap.put("errInfo", "");
        data.put("head", headMap);
        return data;
    }
}
