package com.dkha.communication.httpws.cache;

import com.dkha.common.validate.UtilValidate;
import com.dkha.communication.common.WSProtocalConst;
import com.dkha.communication.httpws.common.FaceCommon;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Spring
 * @Since 2019/8/14 14:22
 * @Description face sdk wappId 缓存
 *
 */

public class WappIdCache {

    public static final Logger logger = LoggerFactory.getLogger(WappIdCache.class);

    /**
     * 初始化连接次数--2次
     */
    public static final int INIT_CONNECTION_TIMES = 2;

    /**
     * WAPPID cache
     */
    public static final Map<String, Integer> WAPPID_CACHE = new ConcurrentHashMap<>();
    /**
     * wappId对应连接次数
     */
    public static final Map<String, Integer> CONNECTION_TIMES = new ConcurrentHashMap<>();

    /**
     * 获取可用的wappid,如果返回结果为0，标识链路未连接成功
     * @param wappidType {@link WappIdTypeEnum}
     * @return
     */
    public static final int getAvailableWappId(String wappidType) {

            if (UtilValidate.isEmpty(WAPPID_CACHE.get(wappidType))) {
                //如无可用WAPPID,则返回0，调用该接口线程休眠500毫秒，等待初始化网络连接完成
                logger.info("无可用WAPPID 当前wappid请求类型为  {}", wappidType);
                return 0;
            }
            return WAPPID_CACHE.get(wappidType);
    }

    /**
     * 设置对应通道关闭
     * @param wappIdType
     */
    public static void shutDown(String wappIdType) {
        WAPPID_CACHE.put(wappIdType, 0);
    }

    /**
     * 连接成功后将当前wappId放入缓存
     * @param wappIdType
     * @param wappId
     */
    public static void setWappId(String wappIdType, int wappId) {
        WAPPID_CACHE.put(wappIdType, wappId);
    }

    /**
     * 连接成功后将当前wappId放入缓存
     * @param wappIdType
     * @param headJson
     */
    public static void setWappId(String wappIdType, String headJson) {
        WAPPID_CACHE.put(wappIdType, FaceCommon.getWappIdFromHeads(headJson));
    }

    /**
     * 判断当前链路是否激活
     * @param wappidType
     */
    public static boolean isActive(String wappidType) {
        Integer wappId = WAPPID_CACHE.get(wappidType);
        if (UtilValidate.isEmpty(wappId)) {
            return false;
        } else if (wappId == 0) {
            return false;
        }
        return true;
    }

    /**
     * 是否初始化完成
     * @param wappIdType
     * @return
     */
    public static boolean isInitializationComplete(String wappIdType) {
        Integer connectionTimes = CONNECTION_TIMES.get(wappIdType);
        if (UtilValidate.isEmpty(connectionTimes)) {
            return false;
        }
        if (connectionTimes.intValue() >= INIT_CONNECTION_TIMES) {
            return true;
        }
        return false;
    }

    /**
     * 增加连接次数
     * @param wappIdType
     */
    public static void increamConnectionTimes(String wappIdType) {
        Integer connectionTimes = CONNECTION_TIMES.get(wappIdType);
        if (UtilValidate.isEmpty(connectionTimes)) {
            CONNECTION_TIMES.put(wappIdType, 1);
        } else {
            CONNECTION_TIMES.put(wappIdType, ++connectionTimes);
        }
    }

    /**
     * 获取连接次数
     * @param wappIdType
     */
    public static int getConnectionTimes(String wappIdType) {
        Integer connectionTimes = CONNECTION_TIMES.get(wappIdType);
        if (UtilValidate.isEmpty(connectionTimes)) {
            return 0;
        } else {
            return connectionTimes.intValue();
        }
    }

    /**
     * 获取缓存的数量
     * @return
     */
    public static final int getCacheSize() {
        return WAPPID_CACHE.size();
    }

    /**
     * wappid类型枚举
     */
    public enum WappIdTypeEnum {
        VIDEO("video", "视频处理通道"),
        PICTURE("picture", "图片处理通道");

        public String code;
        public String name;

        WappIdTypeEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getValueByCode(String code) {
            for (WappIdTypeEnum platformFree : WappIdTypeEnum.values()) {
                if (code.equals(platformFree.code)) {
                    return platformFree.name;
                }
            }
            return null;
        }
    }
}
