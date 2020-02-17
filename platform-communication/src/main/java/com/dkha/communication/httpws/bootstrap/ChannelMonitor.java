package com.dkha.communication.httpws.bootstrap;

import com.dkha.common.util.DateUtils;
import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.httpws.client.PictureWebSocketClient;
import com.dkha.communication.httpws.client.VideoWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Spring
 * @Since 2019/12/6 15:23
 * @Description 链路监视器--如果链路down掉，则30秒内重启
 */
@Component
@EnableScheduling
public class ChannelMonitor {

    public static final Logger logger = LoggerFactory.getLogger(ChannelMonitor.class);

    @Autowired
    private PictureWebSocketClient pictureSocketClient;
    @Autowired
    private VideoWebSocketClient videoSocketClient;

    /**
     * 30秒检查一次链路，如果链路断掉，则重启链路
     * initialDelay = 60000 定时器，延迟启动一分钟 单位毫秒
     * fixedDelay = 30000 上一次任务执行完30秒后再调度执行
     */
   /* @Scheduled(initialDelay = 60000, fixedDelay = 30000)
    public void channelCheck() throws Exception {
        if (!WappIdCache.isActive(WappIdCache.WappIdTypeEnum.PICTURE.code)) {
            logger.info("[{} 通道重启中 重启时间 {}]", WappIdCache.WappIdTypeEnum.PICTURE.name, DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
            pictureSocketClient.open(WappIdCache.WappIdTypeEnum.PICTURE.code);
        }

        if (!WappIdCache.isActive(WappIdCache.WappIdTypeEnum.VIDEO.code)) {
            logger.info("[{} 通道重启中 重启时间 {}]", WappIdCache.WappIdTypeEnum.VIDEO.name, DateUtils.date2Str(new Date(), DateUtils.datetimeFormat));
            videoSocketClient.open(WappIdCache.WappIdTypeEnum.VIDEO.code);
        }
    }*/
}
