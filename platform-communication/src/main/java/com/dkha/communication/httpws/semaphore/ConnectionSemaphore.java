package com.dkha.communication.httpws.semaphore;

import com.dkha.common.redis.RedisKeys;
import com.dkha.common.redis.RedisUtils;
import com.dkha.common.util.DateUtils;
import com.dkha.common.util.IpUtils;
import com.dkha.common.util.SpringBeanFactoryUtils;
import com.dkha.common.validate.UtilValidate;
import com.dkha.communication.httpws.cache.WappIdCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Spring
 * @Since 2019/12/2 10:02
 * @Description 通道连接信号量
 * 将确定channel创建成功后采取何种操作--只对视频通道做处理
 */
@Component
public class ConnectionSemaphore {

    @Autowired
    private RedisUtils redisUtils;
    /**
     * 初始化连接次数--2次
     */
    public static final int INIT_CONNECTION_TIMES = 2;
    /**
     * 第一次连接
     */
    public static final int FIRST_CONNECT = 1;

    @Value("${faceServer.name}")
    private String faceServerName;

    /**
     * 判断当前通道是否在初始化
     * @return true 正在初始化
     *          false 初始化完成
     */
    public Boolean isInit(String channelType) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        //第一次连接
        if (UtilValidate.isEmpty(cacheObj)) {
           return true;
        } else {
            ConnectionParams connectParams = (ConnectionParams)cacheObj;
            if (connectParams.getConnectTimes() >= INIT_CONNECTION_TIMES) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 是否是第一次连接握手完成
     * @return
     */
    public boolean isFirstConnectSuccess() {
        Object pictureCache = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, WappIdCache.WappIdTypeEnum.PICTURE.code));
        Object videoCache = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, WappIdCache.WappIdTypeEnum.VIDEO.code));
        if (UtilValidate.isEmpty(pictureCache)) {
            return false;
        }
        if (UtilValidate.isEmpty(videoCache)) {
            return false;
        }
        ConnectionParams pictureConnectionParams = (ConnectionParams)pictureCache;
        ConnectionParams videoConnectionParams = (ConnectionParams)videoCache;
        //为0标识redis值状态未设置成功/连接失败
        if (pictureConnectionParams.getWappId() != 0 && videoConnectionParams.getWappId() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是第一次连接
     * @param channelType
     * @return
     */
    public boolean isFirstConnect(String channelType) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        if (UtilValidate.isEmpty(cacheObj)) {
            return true;
        } else {
            ConnectionParams connectParams = (ConnectionParams)cacheObj;
            if (connectParams.getConnectTimes() == FIRST_CONNECT) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 第一次连接是否成功
     * @param channelType
     * @return
     */
    public boolean isFirstConnectSuccess(String channelType) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        if (UtilValidate.isEmpty(cacheObj)) {
            return false;
        } else {
            ConnectionParams connectParams = (ConnectionParams)cacheObj;
            if (connectParams.getConnectTimes() >= FIRST_CONNECT) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 链路是否激活
     * @param channelType 通道类型 {@link com.dkha.communication.httpws.cache.WappIdCache.WappIdTypeEnum}
     * @return
     */
    public boolean isActive(String channelType) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        //第一次连接
        if (UtilValidate.isEmpty(cacheObj)) {
            return false;
        } else {
            ConnectionParams connectParams = (ConnectionParams)cacheObj;
            if (connectParams.isDown) {
                return false;
            } else {
                if (connectParams.getConnectTimes() >= INIT_CONNECTION_TIMES) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * 设置链路断开
     * @param channelType
     */
    public void shutDown(String channelType) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        if (UtilValidate.isEmpty(cacheObj)) {
            return;
        }
        ConnectionParams connectParams = (ConnectionParams)cacheObj;
        connectParams.setDown(true);
        redisUtils.set(RedisKeys.getChannelCacheKey(faceServerName, channelType), connectParams);
    }

    /**
     * 设置链路激活
     * @param channelType
     */
    public void setActive(String channelType) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        if (UtilValidate.isEmpty(cacheObj)) {
            return;
        }
        ConnectionParams connectParams = (ConnectionParams)cacheObj;
        connectParams.setDown(false);
        redisUtils.set(RedisKeys.getChannelCacheKey(faceServerName, channelType), connectParams);
    }

    /**
     * 获取连接次数
     * @param channelType
     * @return
     */
    public int getConnectionTimes(String channelType) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        //第一次连接
        if (UtilValidate.isEmpty(cacheObj)) {
            return 1;
        } else {
            ConnectionParams connectParams = (ConnectionParams)cacheObj;
            return connectParams.getConnectTimes();
        }
    }

    /**
     * 获取对应wappId
     * @param channelType
     * @return
     */
    public int getWappId(String channelType) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        if (UtilValidate.isEmpty(cacheObj)) {
            return 0;
        }
        return ((ConnectionParams) cacheObj).getWappId();
    }

    /**
     * 设置wappId
     * @param channelType
     * @param wappId
     */
    public void setWappId(String channelType, int wappId) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        ConnectionParams connectionParams;
        if (UtilValidate.isEmpty(cacheObj)) {
            connectionParams = new ConnectionParams();
            //第一次连接
            connectionParams.setDown(true);
        } else {
            connectionParams = (ConnectionParams) cacheObj;
            //非第一次
            connectionParams.setDown(false);
        }
        connectionParams.setServerName(faceServerName);
        connectionParams.setChannelType(channelType);
        connectionParams.setWappId(wappId);
        connectionParams.setConnectionTime(DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
        redisUtils.set(RedisKeys.getChannelCacheKey(faceServerName, channelType), connectionParams);
    }

    /**
     * 增加连接次数
     */
    public void increaseConnectionTimes(String channelType, int wappId) {
        Object cacheObj = redisUtils.get(RedisKeys.getChannelCacheKey(faceServerName, channelType));
        if (UtilValidate.isEmpty(cacheObj)) {
            ConnectionParams connectionParams = new ConnectionParams();
            //连接次数加一
            connectionParams.setConnectTimes(connectionParams.getConnectTimes() + 1);
            connectionParams.setServerName(faceServerName);
            connectionParams.setChannelType(channelType);
            connectionParams.setWappId(wappId);
            connectionParams.setConnectionTime(DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
            //redis expire time -1 不设置过期时长
            redisUtils.set(RedisKeys.getChannelCacheKey(faceServerName, channelType), connectionParams, -1L);
        } else {
            ConnectionParams connectParams = (ConnectionParams) cacheObj;
            connectParams.setConnectTimes(connectParams.getConnectTimes() + 1);
            connectParams.setConnectionTime(DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
            redisUtils.set(RedisKeys.getChannelCacheKey(faceServerName, channelType), connectParams, -1L);
        }
    }

    /**
     * 重置连接次数----前提是sdk将当前通道对应的WAPPID清除，否则不能调用该接口
     * @param channelType 通道类型
     */
    public void resetConnectionTimes(String channelType) {
        ConnectionParams connectParams = new ConnectionParams();
        //连接次数加一
        connectParams.setServerName(faceServerName);
        connectParams.setChannelType(channelType);
        connectParams.setConnectionTime(DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
        //redis expire time -1 不设置过期时长
        redisUtils.set(RedisKeys.getChannelCacheKey(faceServerName, channelType), connectParams, -1L);
    }
    /**
     * 当前连接通道对应参数
     */
    private static class ConnectionParams {

        /**
         * 当前通道连接次数,只要服务启动成功后，当前连接次数就不能重置
         * 除非sdk将当前通道对应的WAPPID重置,在该种情况下，才能重置连接次数为0
         */
        private int connectTimes = 0;

        /**
         * 服务名称，用于唯一标记当前通道，服务名称请勿使用 ":" 冒号
         */
        private String serverName;

        /**
         * 通道类型 {@link com.dkha.communication.httpws.cache.WappIdCache.WappIdTypeEnum} 中的code
         */
        private String channelType;

        /**
         * wappId
         * 规则：1.第一次以4个全0的APPID去连接服务端，连接成功后，服务端会返回非0的WAPPID
         *       2.后面的连接均使用该WAPPID去连接服务端，直到服务端清除对应WAPPID
         */
        private int wappId;

        /**
         * 更新时间，用于记录channel 启动/重启时间
         */
        private String connectionTime;

        /**
         * 链路是否关闭 true 是  false否
         */
        private boolean isDown;

        public int getConnectTimes() {
            return connectTimes;
        }

        public void setConnectTimes(int connectTimes) {
            this.connectTimes = connectTimes;
        }

        public String getConnectionTime() {
            return connectionTime;
        }

        public void setConnectionTime(String connectionTime) {
            this.connectionTime = connectionTime;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public int getWappId() {
            return wappId;
        }

        public void setWappId(int wappId) {
            this.wappId = wappId;
        }

        public boolean isDown() {
            return isDown;
        }

        public void setDown(boolean down) {
            isDown = down;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }
    }
}
