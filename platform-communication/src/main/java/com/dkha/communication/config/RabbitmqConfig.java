package com.dkha.communication.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
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

    public static final String ACK_STATUS_CHG = "ack.queue.statuschg";
    public static final String ACK_STATUSCHG_EXCHANGE = "ack.statuschg.exchange";


    /**报警队列*/
    @Bean
    public Queue immediateQueueOne() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(ACK_ALARM);
    }
    /**状态改变队列*/
    @Bean
    public Queue immediateQueueTwo() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(ACK_STATUS_CHG);
    }
    @Bean
    public FanoutExchange ackFanoutExchangeOne() {
        return new FanoutExchange(ACK_EXCHANGE);
    }
    @Bean
    public FanoutExchange ackFanoutExchangeTwo() {
        return new FanoutExchange(ACK_STATUSCHG_EXCHANGE);
    }

    @Bean
    public Binding ackBindingA() {
        return BindingBuilder.bind(immediateQueueOne()).to(ackFanoutExchangeOne());
    }
    @Bean
    public Binding ackBindingB() {
        return BindingBuilder.bind(immediateQueueTwo()).to(ackFanoutExchangeTwo());
    }
}
