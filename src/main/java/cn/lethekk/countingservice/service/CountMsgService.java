package cn.lethekk.countingservice.service;

import cn.lethekk.countingservice.entity.UserRequestMsg;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

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

    public void countEvent(Long videoId, Integer eventTypeCode) {    //eventType: view like share
        UserRequestMsg msg = UserRequestMsg.builder()
                .videoId(videoId)
                .eventTypeCode(eventTypeCode)
                .time(LocalDateTime.now())
                .build();
        String msgJson = new Gson().toJson(msg);
        for (int i = 0; i < 10000; i++) {
            rabbitTemplate.convertAndSend(requestExchange, routingKey, msgJson);
        }
        rabbitTemplate.convertAndSend(requestExchange, routingKey, msgJson);
        log.info("请求转发消息成功 : " + msgJson);
    }

    //测试消息成功发送
//    @RabbitListener(queues = {userRequestQueue})
//    public void receive3(@Payload String msgJson) {
//        UserRequestMsg msg = new Gson().fromJson(msgJson, UserRequestMsg.class);
//        log.info("userRequestQueue队列 : " + msg.toString());
//    }

}
