package com.dkha.communication.httpws.client;

import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.httpws.cache.WsResultCache;
import com.dkha.communication.httpws.handlers.WebSocketClientHandler;
import com.dkha.communication.httpws.semaphore.ConnectionSemaphore;
import com.dkha.communication.services.ResultProtocolAnalysis;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Spring
 * http://stephen.genoprime.com
 */
@Component
@PropertySource({"classpath:system.properties"})
public class PictureWebSocketClient {

    public static final Logger logger = LoggerFactory.getLogger(PictureWebSocketClient.class);
    private Channel channel;
    private EventLoopGroup group;
    /**
     * wappId type
     * {@link WappIdCache.WappIdTypeEnum}
     */
    private String wappIdType;

    /**
     * 服务器webSocket 地址前缀
     */
    @Value("${weiyun.url}")
    private String serverUrl;
    @Value("${server.port}")
    private String nativePort;
    @Autowired
    private WsResultCache wsResultCache;
    /**
     * 结果解析
     */
    @Autowired
    ResultProtocolAnalysis resultProtocolAnalysis;
    @Autowired
    private ConnectionSemaphore connectionSemaphore;

    /**
     * create connect
     *
     * @param wappIdType 类型 {@link WappIdCache.WappIdTypeEnum}
     * @throws Exception
     */
    public void open(String wappIdType) throws Exception {
        this.wappIdType = wappIdType;
        Bootstrap bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        URI uri = this.getURI();
        String protocol = uri.getScheme();
        if (!"ws".equals(protocol)) {
            logger.info("[不支持协议类型-->]" + protocol);
        }

        /**
         * Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
         * If you change it to V00, ping is not supported and remember to change
         * HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
         */
        WebSocketClientHandler handler =
                new WebSocketClientHandler(
                        WebSocketClientHandshakerFactory.newHandshaker(
                                uri, WebSocketVersion.V13, null, false, HttpHeaders.EMPTY_HEADERS, 1280000), wsResultCache, wappIdType, uri,resultProtocolAnalysis, connectionSemaphore, nativePort);

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("http-codec", new HttpClientCodec());
                        //处理数据最大长度
                        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                        pipeline.addLast("ws-handler", handler);
                    }
                });

        logger.info("[WebSocket Client connecting]");
        //连接服务端，使用同步方式绑定服务端监听端口
        channel = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();

        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                logger.info("[{} Picture WebSocket Client close complete]", channel.toString());
                group.shutdownGracefully();
                open(WappIdCache.WappIdTypeEnum.PICTURE.code);
            }
        });
        handler.handshakeFuture().sync();
    }

    /**
     * close
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        logger.info("WebSocket Client sending close");
        channel.writeAndFlush(new CloseWebSocketFrame());
        channel.closeFuture().sync();
    }

    /**
     * send message
     *
     * @param message
     */
    public void sendMessage(String message) {
        logger.info("[Picture 客户端发送消息-->{}]", message);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    /**
     * get wappId
     *
     * @return
     */
    /**
     * get wappId
     * @return
     */
    private URI getURI() throws URISyntaxException {
        int wappId = WappIdCache.getAvailableWappId(wappIdType);
        //初次连接时候appId为0000
        String requestUrl;
        if (wappId == 0) {
            wappId = connectionSemaphore.getWappId(wappIdType);
            //本机第一次初始化连接
            if (wappId == 0) {
                requestUrl = serverUrl + "?appid=0000";
            } else {
                requestUrl = serverUrl + "?appid=" + wappId;
            }
        } else {
            requestUrl = serverUrl + "?appid=" + wappId;
        }
        logger.info(requestUrl);
        return new URI(requestUrl);
    }
}
