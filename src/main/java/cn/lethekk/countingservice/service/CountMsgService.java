package cn.lethekk.countingservice.service;

import cn.lethekk.countingservice.entity.UserRequestMsg;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static cn.lethekk.countingservice.constant.MqConstant.*;

/**
 * @author lethe
 * @date 2023/5/15 20:24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CountMsgService {

    private final RabbitTemplate rabbitTemplate;

    public void countEvent(Long videoId, Integer eventTypeCode)  {    //eventType: view like share
        UserRequestMsg msg = UserRequestMsg.builder()
                .videoId(videoId)
                .eventTypeCode(eventTypeCode)
                .time(LocalDateTime.now())
                .build();
        //sendPublishConfirm(msg);
//        log.info(String.valueOf(rabbitTemplate.isChannelTransacted()));
//        rabbitTemplate.setChannelTransacted(true);
//        log.info(String.valueOf(rabbitTemplate.isChannelTransacted()));
        // 将 Channel 对象设置到 RabbitTemplate
        /**
         * channel数量上限 在MQ服务器那边也要配， 和本地应用一起取更小的值
         * rabbitTemplate.invoke每次创建一个channel 考虑如何复用
         */
        for (int i = 0; i < 2000; i++) {  //todo 1000000会报错 org.springframework.amqp.AmqpResourceNotAvailableException: The channelMax limit is reached. Try later.
            sendAutoSerialization2(msg);
            log.info("idx:" + i);
        }
        sendAutoSerialization2(msg);
        log.info("请求转发消息成功 : " + msg);
    }

    /**
     * 手动序列化发送消息
     * 默认只支持String,byte[] 这些基本类型序列化
     */
    public void sendManualSerialization(UserRequestMsg msg) {
        String msgJson = new Gson().toJson(msg);
        rabbitTemplate.convertAndSend(requestExchange, routingKey, msgJson);
    }

    /**
     * 通过配置RabbitMQ序列化，可以直接发送Object对象
     */
    public void sendAutoSerialization(UserRequestMsg msg) {
        rabbitTemplate.convertAndSend(requestExchange, routingKey, msg);
        boolean sendSuccess = rabbitTemplate.waitForConfirms(1000L);
        log.info("消息发送结果为: " + sendSuccess);
        if(!sendSuccess) {
            throw new RuntimeException("消息发送失败或超时");
        }
    }

    /**
     * 发送消息，自动学序列化，同步确认
     */
    public void sendAutoSerialization2(UserRequestMsg msg) {
        Boolean sendSuccess = rabbitTemplate.invoke(operations -> {
            rabbitTemplate.convertAndSend(requestExchange, routingKey, msg);
            return rabbitTemplate.waitForConfirms(1000L);
        });
        log.info("消息发送结果为: " + sendSuccess);
        if(sendSuccess == null || !sendSuccess) {
            throw new RuntimeException("消息发送失败或超时");
        }
    }

    /**
     * 事务性消息
     */
    @Transactional
    public void sendTransaction(UserRequestMsg msg) {
        rabbitTemplate.convertAndSend(requestExchange, routingKey, msg);
        //int i = 1/0;
    }

    /**
     * 发送方确认机制发送消息
     */
    public void sendPublishConfirm(UserRequestMsg msg) {
        rabbitTemplate.convertAndSend(requestExchange, routingKey, msg, new CorrelationData());
//        rabbitTemplate.convertAndSend(requestExchange + "1", routingKey, msg, new CorrelationData());
    }

    //测试消息成功发送
//    @RabbitListener(queues = {userRequestQueue})
//    public void receive3(@Payload String msgJson) {
//        UserRequestMsg msg = new Gson().fromJson(msgJson, UserRequestMsg.class);
//        log.info("userRequestQueue队列 : " + msg.toString());
//    }

}
