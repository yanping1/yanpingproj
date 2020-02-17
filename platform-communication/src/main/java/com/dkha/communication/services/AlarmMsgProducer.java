package com.dkha.communication.services;

import com.alibaba.fastjson.JSONObject;
import com.dkha.communication.common.WSProtocalConst;
import com.dkha.communication.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @version V1.0
 * @Description:
 * @Title:
 * @author: huangyugang
 * @date: 2019/12/10 17:40
 * @Copyright: 成都电科慧安
 */
@Component
@Slf4j
public class AlarmMsgProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送报警信息
     * @param msg
     * @param timestampstr
     */
    public void ackAlarmMQSender(String msg,String timestampstr) {
         // 生产者发送消息到exchange后没有绑定的queue时将消息退回
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            if(log.isInfoEnabled()) {
                log.info("ackMQSender 发送消息被退回" + exchange + routingKey);
            }
        });
          // 生产者发送消息confirm检测
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                if(log.isDebugEnabled()) {
                    System.out.println("ackMQSender 消息发送失败" + cause + correlationData.toString());
                }
            } else {
                if(log.isDebugEnabled()) {
                    System.out.println("ackMQSender 消息发送成功 ");
                }
            }
        });

        String  alarmmsg = String.format("{\"timestamp\":%s,\"loaddata\":%s}", timestampstr, msg);

        this.rabbitTemplate.convertAndSend(RabbitmqConfig.ACK_EXCHANGE, "", alarmmsg);
    }

    /**
     * 发送状态改变信息
     * @param msg
     * @param timestampstr
     */
    public void ackStatusMQSender(String msg,String timestampstr) {
        String statusmsg = String.format("{\"timestamp\":%s,\"loaddata\":%s}", timestampstr, msg);
        this.rabbitTemplate.convertAndSend(RabbitmqConfig.ACK_STATUSCHG_EXCHANGE, "", statusmsg);
    }

}
