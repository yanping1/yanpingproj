package com.dkha.communication.httpws.clientnew;

import com.dkha.communication.httpws.cache.ChannelCache;
import com.dkha.communication.httpws.cache.WappIdCache;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @Author Spring
 * @Since 2019/12/16 17:35
 * @Description 链路监视器，当发现当前的链路不稳定关闭之后，将进行重连
 */
@Slf4j
@ChannelHandler.Sharable
public abstract class ConnectionMonitor extends ChannelInboundHandlerAdapter implements TimerTask, ChannelHandlerHolder{

    private final Bootstrap bootstrap;
    private URI uri;
    private String wappIdType;
    private final Timer timer;

    public ConnectionMonitor(Bootstrap bootstrap, URI uri, String wappIdType, Timer timer) {
        this.bootstrap = bootstrap;
        this.uri = uri;
        this.wappIdType = wappIdType;
        this.timer = timer;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //设置信号量状态为shutdown
        WappIdCache.shutDown(wappIdType);
        /**
         * 链路连接成功后，down掉，重连服务端
         */
        if (WappIdCache.isInitializationComplete(wappIdType)) {
            log.info("[{} 断开，即将开始重连]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType));
            timer.newTimeout(this, 5000, TimeUnit.MILLISECONDS);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        this.reConnect();
    }

    /**
     * 发起重连
     */
    private void reConnect() {
        ChannelFuture future;
        synchronized (bootstrap) {
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) {
                    channel.pipeline().addLast(handlers());
                }
            });

            future = bootstrap.connect(uri.getHost(), uri.getPort());
        }

        //监听结果
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                boolean succeed = future.isSuccess();
                //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
                if (!succeed) {
                    log.info("[{} 通道重连失败]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType));
                    future.channel().pipeline().fireChannelInactive();
                }else{
                    log.info("[{} 重连成功]", WappIdCache.WappIdTypeEnum.getValueByCode(wappIdType));
                    ChannelCache.put(wappIdType, future.channel());
                }
            }
        });
    }
}
