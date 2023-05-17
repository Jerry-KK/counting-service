package cn.lethekk.countingservice.rabbitdemo;

import org.springframework.amqp.core.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lethe
 * @date 2023/5/15 21:27
 */

//测试rabbit时创建的类
@RequiredArgsConstructor
@RestController
@RequestMapping("/send")
public class SendMsgController {

    private final RabbitTemplate rabbitTemplate;
    private final Queue myQueue;

    @GetMapping("/msg")
    public String sendMsg(@RequestParam("msg") String msg){
        rabbitTemplate.convertAndSend(myQueue.getName(),msg);
        return "OK";
    }

    @GetMapping("/msg/key")
    public String sendMsg(@RequestParam("msg") String msg, @RequestParam("routeKey") String routeKey){
        rabbitTemplate.convertAndSend("topicExchange",routeKey,msg);
        return "OK";
    }



}
