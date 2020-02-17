package com.dkha.communication.httpws.bootstrap;

import com.dkha.communication.httpws.client.PictureWebSocketClient;
import com.dkha.communication.httpws.client.VideoWebSocketClient;
import com.dkha.communication.httpws.queue.RequestMsgQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @Description: TODO:
 * @Title:
 * @Package com.dkha.communication.httpws.bootstrap
 * @author: huangyugang
 * @date: 2019/11/29 17:10
 * @Copyright: 成都电科慧安
 */

public class SendBufferMSGThread implements CommandLineRunner {

    @Autowired
    RequestMsgQueue requestMsgQueue;

    @Autowired
    VideoWebSocketClient videoWebSocketClient;

    @Override
    public void run(String... args) {
        while(true){
          String Str = requestMsgQueue.getMsgFromQueeu();

        }
    }
}
