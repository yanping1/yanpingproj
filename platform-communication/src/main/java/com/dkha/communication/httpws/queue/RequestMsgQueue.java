package com.dkha.communication.httpws.queue;

import com.dkha.communication.controllers.HttpWsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @Description: WEB-API层和底层SDK接口层的消息队列机制
 * @Title:
 * @Package com.dkha.communication.httpws.queue
 * @author: huangyugang
 * @date: 2019/11/29 13:37
 * @Copyright: 成都电科慧安
 */
@Component
public class RequestMsgQueue {
    public static final Logger logger = LoggerFactory.getLogger(RequestMsgQueue.class);
    /**
     * 消息请求队列
     */
    private static BlockingQueue<String> remsgqueue = new LinkedBlockingQueue(1500);

    /**
     * 消息入队
     */
    public boolean sendMsgToQueue(String msg) {
        try {
            boolean b= remsgqueue.offer(msg, 5, TimeUnit.SECONDS);
            System.out.println("入队正确："+b+msg);
            return b;
        } catch (Exception ex) {
            logger.error("消息入队异常：", ex);
        }
        return false;
    }

    /**
     * 消息出队
     */
    public String getMsgFromQueeu() {
        try {
            return remsgqueue.poll(5, TimeUnit.SECONDS);
        } catch (Exception ex) {
            logger.error("消息出队异常：", ex);
        }
        return "";
    }

}
