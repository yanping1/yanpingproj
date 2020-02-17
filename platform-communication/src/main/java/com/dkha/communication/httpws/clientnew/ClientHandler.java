package com.dkha.communication.httpws.clientnew;

import com.dkha.common.util.DateUtils;
import com.dkha.communication.httpws.cache.ChannelCache;
import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.httpws.common.FaceCommon;
import com.dkha.communication.httpws.semaphore.ConnectionSemaphore;
import com.dkha.communication.services.ResultProtocolAnalysis;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Date;

/**
 * @Author Spring
 * @Since 2019/12/16 17:28
 * @Description 客户端handler
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketClientHandshaker handShaker;
    private ChannelPromise handshakeFuture;
    private URI uri;
    private String wappIdType;
    private ResultProtocolAnalysis resultProtocolAnalysis;
    private ConnectionSemaphore connectionSemaphore;

    public ClientHandler(WebSocketClientHandshaker handShaker, URI uri, String wappIdType, ResultProtocolAnalysis resultProtocolAnalysis, ConnectionSemaphore connectionSemaphore) {
        this.handShaker = handShaker;
        this.uri = uri;
        this.wappIdType = wappIdType;
        this.resultProtocolAnalysis = resultProtocolAnalysis;
        this.connectionSemaphore = connectionSemaphore;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        //连接激活后给handShaker设置值，方便重连
        handShaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, false, HttpHeaders.EMPTY_HEADERS, 65536);
        handshakeFuture = ctx.newPromise();
        log.info("[webSocket客户端 {} 创建连接-->开始握手]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType));
        handShaker.handshake(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        final Channel ch = channelHandlerContext.channel();
        /**
         * 设置握手完成
         */
        if (!handShaker.isHandshakeComplete()) {
            // web socket client connected
            handShaker.finishHandshake(ch, (FullHttpResponse) o);
            handshakeFuture.setSuccess();
            log.info("[webSocket客户端 {} 连接成功-->握手成功]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType));
            return;
        }

        /**
         * HTTP 协议处理
         */
        if (o instanceof FullHttpResponse) {
            final FullHttpResponse response = (FullHttpResponse) o;
            log.info("[当前只支持webSocket]");
            throw new Exception("Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }
        /**
         * 处理webSocket返回
         */
        handlerWebSocketFrame(channelHandlerContext, (WebSocketFrame)o);
    }

    /**
     * 处理客户端回复消息
     * @param ctx
     * @param frame
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws InterruptedException {
        //判断是否关闭链路指令
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
            return;
        }
        //判断是否pong消息
        if (frame instanceof PongWebSocketFrame) {
            ctx.channel().writeAndFlush(new PingWebSocketFrame(frame.content().retain()));
            return;
        }
        //判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            log.info("[{} 心跳时间 {}]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType), DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //当前只支持文本信息，不支持其他格式消息
        if (!(frame instanceof TextWebSocketFrame)) {
            log.info("当前只支持文本信息，不支持其他格式消息");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 返回消息
        String response = ((TextWebSocketFrame) frame).text();
        log.info("[{} 客户端收到消息：{}]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType), response);
        //将channel放入缓存
        ChannelCache.put(wappIdType, ctx.channel());
        /**
         * 消息处理
         */
        if (!WappIdCache.isActive(wappIdType)) {
            /**
             * 连接初始化
             */
            int wappId = FaceCommon.getWappIdFromHeads(response);
            //连接次数加一
            connectionSemaphore.increaseConnectionTimes(wappIdType, wappId);
            WappIdCache.increamConnectionTimes(wappIdType);
            //第二次握手连接/断线重连
            if (!connectionSemaphore.isFirstConnect(wappIdType)) {
                //第二次连接
                connectionSemaphore.setWappId(wappIdType, wappId);
                //设置本地缓存
                WappIdCache.setWappId(wappIdType, wappId);
                ChannelCache.put(wappIdType, ctx.channel());
                //断开重连，则将WappIdCache对应的缓存
                if (connectionSemaphore.getConnectionTimes(wappIdType) >= ConnectionSemaphore.INIT_CONNECTION_TIMES) {
                    WappIdCache.increamConnectionTimes(wappIdType);
                }
            }
        } else {
            /**
             * 消息处理
             */
            resultProtocolAnalysis.ResultProtocolRresolution(response);
        }
    }
}
