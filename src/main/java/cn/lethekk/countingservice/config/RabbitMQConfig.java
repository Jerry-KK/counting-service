package cn.lethekk.countingservice.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lethe
 * @date 2023/5/17 12:55
 */
@Configuration
public class RabbitMQConfig {

    //自动序列化消息
    /*@Bean
    MessageConverter setMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }*/

    /**
     * Jackson2JsonMessageConverter默认不支持LocalDateTime,需要做一些增强
     * @return
     */
    @Bean
    MessageConverter setMessageConverter() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(om);
    }

}
