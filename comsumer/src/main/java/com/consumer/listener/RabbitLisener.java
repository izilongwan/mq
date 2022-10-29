package com.consumer.listener;

import java.util.Date;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.common.entity.R;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RabbitLisener {
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.1"), exchange = @Exchange(value = "direct"), key = {
            "1", "0" }))
    public void listener1(R<String> msg) {
        log.info("msg 1 ==> {}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.2"), exchange = @Exchange(value = "direct"), key = {
            "2", "0" }))
    public void lisener2(R<String> msg) {
        log.info("msg 2 ==> {}", msg);
    }

}
