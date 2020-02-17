package com.dkha.communication.httpws.handlers;

import com.dkha.common.util.DateUtils;
import com.dkha.communication.httpws.cache.ChannelCache;
import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.httpws.cache.WsResultCache;
import com.dkha.communication.httpws.common.FaceCommon;
import com.dkha.communication.httpws.semaphore.ConnectionSemaphore;
import com.dkha.communication.services.ResultProtocolAnalysis;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Date;

/**
 * @Description
 * @Author Spring
 * @Since 2019/8/13 15:56
 */
@ChannelHandler.Sharable
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private WebSocketClientHandshaker handShaker;
    private ChannelPromise handshakeFuture;
    private URI uri;
    private WsResultCache wsResultCache;
    private String wappIdType;
    private  ResultProtocolAnalysis resultProtocolAnalysis;
    private ConnectionSemaphore connectionSemaphore;
    private String port;

    public WebSocketClientHandler(final WebSocketClientHandshaker handShaker, WsResultCache wsResultCache, String wappIdType, URI uri, ResultProtocolAnalysis resultProtocolAnalysis, ConnectionSemaphore connectionSemaphore, String port) {
        this.handShaker = handShaker;
        this.wsResultCache = wsResultCache;
        this.wappIdType = wappIdType;
        this.uri = uri;
        this.resultProtocolAnalysis=resultProtocolAnalysis;
        this.connectionSemaphore = connectionSemaphore;
        this.port = port;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        logger.info("[webSocket客户端 {} 创建连接-->开始握手]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType));
        handShaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //链路没激活的情况下，不做下面的信号量设置
        if (WappIdCache.isActive(wappIdType)) {
            logger.info("通道断开连接 断开时间 {}", DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
            ctx.close();
            super.channelInactive(ctx);
            //设置信号量
            connectionSemaphore.shutDown(wappIdType);
            WappIdCache.shutDown(wappIdType);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        final Channel ch = ctx.channel();
        /**
         * 设置握手完成
         */
        if (!handShaker.isHandshakeComplete()) {
            // web socket client connected
            handShaker.finishHandshake(ch, (FullHttpResponse) msg);
            handshakeFuture.setSuccess();
            logger.info("[webSocket客户端 {} 连接成功-->握手成功]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType));
            return;
        }

        /**
         * HTTP 协议处理
         */
        if (msg instanceof FullHttpResponse) {
            final FullHttpResponse response = (FullHttpResponse) msg;
            logger.info("[当前只支持webSocket]");
            throw new Exception("Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }
        /**
         * 处理webSocket返回
         */
        handlerWebSocketFrame(ctx, (WebSocketFrame)msg);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {

        cause.printStackTrace();

        //将错误信息写入日志
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        cause.printStackTrace(writer);
        String errorMsg = stringWriter.getBuffer().toString();
        logger.error(errorMsg);

        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
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
            logger.info("[{} 心跳时间 {}]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType), DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //当前只支持文本信息，不支持其他格式消息
        if (!(frame instanceof TextWebSocketFrame)) {
            logger.info("当前只支持文本信息，不支持其他格式消息");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 返回消息
        String response = ((TextWebSocketFrame) frame).text();
        logger.info("[{} 客户端收到消息：{}]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType), response);
        /**
         * 对返回结果进行解析
         */
        if (!WappIdCache.isActive(wappIdType)) {
            int wappId = FaceCommon.getWappIdFromHeads(response);
            //第一次连接
            if (connectionSemaphore.isFirstConnect(wappIdType)) {
                connectionSemaphore.increaseConnectionTimes(wappIdType, wappId);
            } else {
                //非第一次连接
                connectionSemaphore.setWappId(wappIdType, wappId);
                connectionSemaphore.increaseConnectionTimes(wappIdType, wappId);
                //设置本地缓存
                WappIdCache.setWappId(wappIdType, wappId);
            }
        } else {
            resultProtocolAnalysis.ResultProtocolRresolution(response);
        }
    }
}
