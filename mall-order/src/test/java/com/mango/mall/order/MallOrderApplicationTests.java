package com.mango.mall.order;

import com.mango.mall.order.entity.OrderSettingEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class MallOrderApplicationTests {

    @Resource
    AmqpAdmin amqpAdmin;
    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        Exchange exchange = new DirectExchange("test-exchange",true,false);
        amqpAdmin.declareExchange(exchange);
        log.info("--------------------");
    }
    @Test
    void createQueue() {
        Queue queue = new Queue("test-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("--------------------");
    }
    @Test
    void createBinding() {
        Binding binding =
                new Binding("test-queue", Binding.DestinationType.QUEUE,"test-exchange","test",null);
        amqpAdmin.declareBinding(binding);
        log.info("--------------------");
    }
    @Test
    void testSendMessage() {
        OrderSettingEntity orderSettingEntity = new OrderSettingEntity();
        orderSettingEntity.setId(100L);
        rabbitTemplate.convertAndSend("test-exchange","test",orderSettingEntity);
        log.info("------send info --------------");
    }

}
