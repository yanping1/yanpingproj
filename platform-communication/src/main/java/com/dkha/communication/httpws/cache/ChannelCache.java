package com.dkha.communication.httpws.cache;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Spring
 * @Since 2019/11/13 13:38
 * @Description channel cache 用于发送消息
 */

public class ChannelCache {

    public static Map<String, Channel> webSocketClientCache = new ConcurrentHashMap<>();

    /**
     * 将当前客户端存放于缓存
     * @param wappIdType
     * @param channel
     */
    public static void put(String wappIdType, Channel channel) {
        webSocketClientCache.put(wappIdType, channel);
    }

    /**
     * 根据类型获取具体的客户端
     * @param wappIdType {@link com.dkha.communication.httpws.cache.WappIdCache.WappIdTypeEnum}
     * @return
     */
    public static Channel get(String wappIdType) {
        return webSocketClientCache.get(wappIdType);
    }

    /**
     * 发送消息
     * @param wappIdType
     * @param message
     */
    public static void sendMessage(String wappIdType, String message) {
        get(wappIdType).writeAndFlush(new TextWebSocketFrame(message));
    }
}
