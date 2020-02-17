package com.dkha.platformasynresultprocess.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @version V1.0
 * @Description: 消息队列
 * @Title:
 * @author: huangyugang
 * @date: 2019/12/10 15:27
 * @Copyright: 成都电科慧安
 */
@Configuration
@Slf4j
public class RabbitmqConfig {
    public static final String ACK_ALARM = "ack.queue.alarm";
    public static final String ACK_EXCHANGE = "ack.alarm.exchange";

    @Bean
    public Queue immediateQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(ACK_ALARM);
    }
    @Bean
    public FanoutExchange ackFanoutExchange() {
        return new FanoutExchange(ACK_EXCHANGE);
    }

    @Bean
    public Binding ackBindingA() {
        return BindingBuilder.bind(immediateQueue()).to(ackFanoutExchange());
    }

}
