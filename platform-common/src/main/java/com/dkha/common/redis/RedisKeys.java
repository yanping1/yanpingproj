/**
 *  (c) .
 *
 *
 *
 *
 */

package com.dkha.common.redis;


/**
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public class RedisKeys {

    /**
     * redis key communication模块
     */
    public static final String KEY_PREFIX_SERVER = "sys:communication:";

    /*********************************************************** communication模块 **********************************************/

    /**
     * 获取通道缓存，通过端口号用于唯一标记通道，一个服务只有一个video通道(长任务通道)
     * @param faceServerName 当前服务名称
     * @param channelType 通道类型
     * @return
     */
    public static String getChannelCacheKey(String faceServerName, String channelType) {
        return KEY_PREFIX_SERVER + "channel:" + faceServerName + ":" + channelType;
    }


}
