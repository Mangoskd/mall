package com.mango.mall.order.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author Mango
 * @Date 2022/6/11 19:29
 */
@EnableRabbit
@Configuration
public class MyRabbitConfig {

    @Resource
    RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause)->{
            System.out.println(correlationData+"-"+ack+cause);
        });
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {

        }));
    }
}
