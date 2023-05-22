package cn.lethekk.countingservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lethe
 * @date 2023/5/17 20:09
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class PublishConfirmImpl implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 修饰一个非静态的void（）方法,在服务器加载Servlet的时候运行，
     * 并且只会被服务器执行一次。
     * 在构造函数之后执行，init（）方法之前执行。
     */
    //目前采用同步确认
    //@PostConstruct
    public void init() {
        log.info("开启PublishConfirm发送方确认");
        rabbitTemplate.setConfirmCallback(this);    //指定 ConfirmCallback
        //rabbitTemplate.setReturnCallback(this);    //指定 ConfirmCallback
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消息发送成功" + correlationData);
        } else {
            System.out.println("消息发送失败:" + correlationData + ":" + cause );
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        // 反序列化对象输出
        log.info("消息主体: {}",new String(returned.getMessage().getBody()));
        log.info("应答码: {}",returned.getReplyCode());
        log.info("描述：{}",returned.getReplyText());
        log.info("消息使用的交换器 exchange : {}",returned.getExchange());
        log.info("消息使用的路由键 routing : {}",returned.getRoutingKey());
    }
}
