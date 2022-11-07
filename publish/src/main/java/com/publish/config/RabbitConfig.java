package com.publish.config;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitConfig implements ApplicationContextAware {
    @Resource
    RabbitTemplate rabbitTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 消息是否到交换机
        rabbitTemplate.setConfirmCallback((CorrelationData c, boolean b, String s) -> {
            if (!b) {
                log.info("{}", c);
                log.info("消息投递到交换机失败 => {}", s);
                return;
            }

            if (!Objects.equals(c, null)) {
                log.info("消息投递到交换机成功 消息id => {}", c.getId());
            }
        });

        // 消息是否到达队列
        rabbitTemplate.setReturnsCallback((ReturnedMessage returned) -> {
            log.info("消息未到达队列 => {}", returned);
        });

        rabbitTemplate.setRecoveryCallback(null);
    }

}
