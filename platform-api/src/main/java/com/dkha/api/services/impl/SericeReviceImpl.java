package com.dkha.api.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.dkha.api.modules.entities.ControlTask;
import com.dkha.api.modules.vo.AlarmStatuVO;
import com.dkha.api.services.IControlTaskService;
import com.dkha.common.validate.UtilValidate;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SericeReviceImpl {

    @Autowired
    private Gson gson;

    @Autowired
    private IControlTaskService iControlTaskService;

    public static final String RESEVICE_INFO="ack.queue.statuschg";
    @RabbitListener(queues = RESEVICE_INFO)
    public void process(String alarmmsg, Channel channel, Message message) throws IOException {
        try {
            if (log.isInfoEnabled()) {
                log.info(RESEVICE_INFO + "receive: " + alarmmsg);
            }
//            Map<String, Object> rsmap = (Map<String, Object>) JSONObject.parseObject(alarmmsg);
            AlarmStatuVO alarmStatuVO=gson.fromJson(alarmmsg,AlarmStatuVO.class);
            if (null!=alarmStatuVO) {
                if(null!=alarmStatuVO.getLoaddata())
                {
//                    String taskId=((Map<String,String>)rsmap.get("loaddata")).get("status").substring("_");
                    if(null!= alarmStatuVO.getLoaddata().getStatus())
                    {
                        alarmStatuVO.getLoaddata().getStatus().forEach((a,b)->
                        {
                            if(!UtilValidate.isEmpty(a))
                            {
                                String taskId=a.substring(0,a.indexOf("_"));
//                                System.out.println(taskId);
//                                ControlTask controlTask=iControlTaskService.getById(taskId);

                                ControlTask builtTask=new ControlTask();
                                builtTask.setRemarks(b.toUpperCase());
                                builtTask.setIdControlTask(taskId);
                                iControlTaskService.getBaseMapper().updateById(builtTask);
                            }
                            //
                        });
                    }

                }
            }
            // 框架容器，是否开启手动ack按照框架配置
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if (log.isDebugEnabled()) {
                log.info(RESEVICE_INFO+ "receive: " + alarmmsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            if (log.isErrorEnabled()) {
                log.info("ACK_QUEUE_A 接受信息异常{}", e.getMessage());
            }
        }

    }



//    @RabbitListener(queuesToDeclare = @Queue("ack.queue.statuschg"))
//    public void receiveComputer(String msg) {
//        log.info(" 测试:{}", msg);
//    }






//    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "panpan1.que"), exchange = @Exchange(value = "panpan1.exchange", type = ExchangeTypes.FANOUT, autoDelete = "true")))
//    public void process2(String msg) {
//        log.info("接收数据0:{}", msg);
//    }
//
//    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "panpan2.que"), exchange = @Exchange(value = "panpan1.exchange", type = ExchangeTypes.FANOUT, autoDelete = "true")))
//    public void process1(String msg) {
//        log.info("接收数据1:{}", msg);
//    }
//
//
//    @RabbitListener(bindings = @QueueBinding(
//            exchange = @Exchange("myOrder"),
//            key = "computer",
//            value = @Queue("computerOrder")
//    ))
//    public void receiveComputer(String msg) {
//        log.info("测试4:{}", msg);
//    }
//
//
//    @RabbitListener(bindings = @QueueBinding(
//            exchange = @Exchange("myOrder"),
//            key = "computer",
//            value = @Queue("computerOrder1")
//    ))
//    public void receiveComputer1(String msg) {
//        log.info(" 测试5:{}", msg);
//    }

//    @RabbitListener(queuesToDeclare = @Queue("ph_queue"))
//    public void receiveComputer6(String msg) {
//        log.info(" 测试6:{}", msg);
//    }
//
//    @RabbitListener(queuesToDeclare = @Queue("ph_queue"))
//    public void receiveComputer5(String msg) {
//        log.info(" 测试7:{}", msg);
//    }


}
