package com.dkha.communication.httpws.clientnew;

import com.dkha.communication.httpws.semaphore.ConnectionSemaphore;
import com.dkha.communication.services.ResultProtocolAnalysis;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * @Author Spring
 * @Since 2019/12/17 13:48
 * @Description 客户端公共父类
 */
@Slf4j
public abstract class Client {

    protected Bootstrap bootstrap;
    protected EventLoopGroup group;
    protected HashedWheelTimer timer;
    protected ConnectionSemaphore connectionSemaphore;
    protected ResultProtocolAnalysis resultProtocolAnalysis;
    protected int maxContentLength;

    /**
     * 连接服务端
     */
    public void connect(URI uri, String wappIdType, int maxContentLength) {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(1);
        timer = new HashedWheelTimer();
        this.maxContentLength = maxContentLength;
        /**
         * Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
         * If you change it to V00, ping is not supported and remember to change
         * HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
         */
        ClientHandler clientHandler = new ClientHandler( WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, false, HttpHeaders.EMPTY_HEADERS, maxContentLength * 1024), uri, wappIdType, resultProtocolAnalysis, connectionSemaphore);

        ConnectionMonitor connectionMonitor = new ConnectionMonitor(bootstrap, uri, wappIdType, timer) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[]{
                        new HttpClientCodec(),
                        //消息最大长度设置为10M
                        new HttpObjectAggregator(maxContentLength * 1024),
                        this,
                        clientHandler,
                };
            }
        };

        ChannelFuture future;
        try {
            synchronized (bootstrap) {
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel channel) throws Exception {
                                channel.pipeline().addLast(connectionMonitor.handlers());
                            }
                        });

                future = bootstrap.connect(uri.getHost(), uri.getPort());
            }

            // 以下代码在synchronized同步块外面是安全的
            future.sync();
            /*clientHandler.handshakeFuture().sync();*/
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //释放资源
            group.shutdownGracefully();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        group.shutdownGracefully();
    }
}
