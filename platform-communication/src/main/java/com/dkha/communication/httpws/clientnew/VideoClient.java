package com.dkha.communication.httpws.clientnew;

import com.dkha.communication.httpws.semaphore.ConnectionSemaphore;
import com.dkha.communication.services.ResultProtocolAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;


/**
 * @Author Spring
 * @Since 2019/12/16 17:26
 * @Description 视频通道客户端
 */
@Slf4j
@Component
public class VideoClient extends Client{

    @Autowired
    private ConnectionSemaphore connectionSemaphore;
    @Autowired
    private ResultProtocolAnalysis resultProtocolAnalysis;

    /**
     * 连接服务端
     */
    public void doConnect(URI uri, String wappIdType, int maxContentLength) {
        super.connectionSemaphore = connectionSemaphore;
        super.resultProtocolAnalysis = resultProtocolAnalysis;
        connect(uri, wappIdType, maxContentLength);
    }
}
