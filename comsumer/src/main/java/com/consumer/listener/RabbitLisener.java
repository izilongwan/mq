package com.consumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitLisener {
    @RabbitListener(queues = "simple.qu")
    public void name(String str) {
        System.out.println(str);
    }
}
