package com.dkha.communication.httpws.bootstrap;

import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.httpws.client.PictureWebSocketClient;
import com.dkha.communication.httpws.client.VideoWebSocketClient;
import com.dkha.communication.httpws.clientnew.PictureClient;
import com.dkha.communication.httpws.clientnew.VideoClient;
import com.dkha.communication.httpws.semaphore.ConnectionSemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author Spring
 * @Since 2019/11/13 11:37
 * @Description 自举程序
 * 启动流程：
 *     1.分别启动一个视频处理通道和一个图片处理通道
 *     2.连接成功后,分别断开连接，然后再次连接(使用新获取到的wappId)
 */
@Component
public class Bootstrap implements CommandLineRunner {

    public static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private ConnectionSemaphore connectionSemaphore;
    @Value("${weiyun.url}")
    private String serverUrl ;
    @Autowired
    private PictureClient pictureClient;
    @Autowired
    private VideoClient videoClient;
    @Value("${netty.maxContentLength}")
    private int nettyMaxContentLength;


    @Override
    public void run(String... args) throws Exception {
        /**
         * 第一次连接
         */

        logger.info("[客户端开始启动……]");
        /**
         * 第一次连接
         */
        pictureClient.doConnect(this.getURI(WappIdCache.WappIdTypeEnum.PICTURE.code), WappIdCache.WappIdTypeEnum.PICTURE.code, nettyMaxContentLength);
        videoClient.doConnect(this.getURI(WappIdCache.WappIdTypeEnum.VIDEO.code), WappIdCache.WappIdTypeEnum.VIDEO.code, nettyMaxContentLength);

        while (true) {
            //两个连接都完成
            Thread.sleep(1000);
            if (connectionSemaphore.isFirstConnectSuccess()) {
                break;
            }
        }
        /*Thread.sleep(1000);*/

        /**
         * 初始化过程中，进行第二次握手
         */
        if (connectionSemaphore.isInit(WappIdCache.WappIdTypeEnum.PICTURE.code)) {
            logger.info("链路初始化中……{} 第一次连接完成，即将开启第二次连接", WappIdCache.WappIdTypeEnum.getValueByCode(WappIdCache.WappIdTypeEnum.PICTURE.code));
            pictureClient.close();
            pictureClient.doConnect(this.getURI(WappIdCache.WappIdTypeEnum.PICTURE.code), WappIdCache.WappIdTypeEnum.PICTURE.code, nettyMaxContentLength);
        }
        if (connectionSemaphore.isInit(WappIdCache.WappIdTypeEnum.VIDEO.code)) {
            logger.info("{} 第一次连接完成，即将开启第二次连接", WappIdCache.WappIdTypeEnum.getValueByCode(WappIdCache.WappIdTypeEnum.VIDEO.code));
            videoClient.close();
            videoClient.doConnect(this.getURI(WappIdCache.WappIdTypeEnum.VIDEO.code), WappIdCache.WappIdTypeEnum.VIDEO.code, nettyMaxContentLength);
        }
    }

    /**
     * get wappId
     * @return
     */
    private URI getURI(String wappIdType) throws URISyntaxException {
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
        logger.info("[{} request url:  {}]", wappIdType, requestUrl);
        return new URI(requestUrl);
    }
}
