package cn.lethekk.countingservice.rabbitdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author lethe
 * @date 2023/5/15 21:18
 */
//测试rabbit时创建的类
@Slf4j
@Service
public class QueueService {

    @RabbitListener(queues = {"ss007"})
    public void receive(@Payload String fileBody) {
        log.info("ss007队列 : " + fileBody);
    }


    @RabbitListener(queues = {"topicQueue1"})
    public void receive2(@Payload String fileBody) {
        log.info("topicQueue1队列 : " + fileBody);
    }

    @RabbitListener(queues = {"topicQueue2"})
    public void receive3(@Payload String fileBody) {
        log.info("topicQueue2队列 : " + fileBody);
    }
}
