package cn.lethekk.countingservice.rabbitdemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lethe
 * @date 2023/5/15 21:25
 */
//测试rabbit时创建的类
@Configuration
public class RabbitConfig {

    @Bean
    public Queue myQueue() {
        return new Queue("ss007", true);
    }


    @Bean
    public Queue topicQueue1(){
        return new Queue("topicQueue1",true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue("topicQueue2",true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange())
                .with("ss007.id.*");
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange())
                .with("ss007.name.*");
    }

}
