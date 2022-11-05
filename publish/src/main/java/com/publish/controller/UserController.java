package com.publish.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.common.entity.R;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("p")
@Slf4j
public class UserController {
    @Resource
    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void name() {
        // publish异常消息处理
        // exchange处理模式
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((CorrelationData c, boolean b, String s) -> {
            if (!b) {
                log.info("{}", c);
                log.info(s);
            }
        });

        rabbitTemplate.setReturnsCallback((ReturnedMessage returned) -> {
            log.info("{}", returned);
        });
    }

    @GetMapping("")
    public R<R<String>> user() {
        R<String> v = new R<String>("1");
        return R.SUCCESS(v);
    }

    @GetMapping("{id}")
    public R<R<String>> id(@PathVariable String id) {
        R<String> v = new R<String>(id);

        String queueName = "simple.qu";
        R<String> rs = R.SUCCESS(id);
        String msg = JSON.toJSONString(rs);

        rabbitTemplate.convertAndSend(queueName, msg);

        return R.SUCCESS(v);
    }

    @GetMapping("direct/{id}")
    public R<R<String>> id1(@PathVariable String id) throws Exception {
        R<String> v = new R<String>(id);

        String exchange = "direct";
        String key = id;
        R<String> rs = R.SUCCESS(id);

        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(exchange, key, rs);
        }

        return R.SUCCESS(v);
    }

    @GetMapping("delay/{id}/{delay}")
    public R<R<String>> id2(@PathVariable String id, @PathVariable String delay) throws Exception {
        R<String> v = new R<String>(id);

        String exchange = "ttl.exchange";
        String key = id;
        final R<String> rs = R.SUCCESS(id);

        for (int i = 0; i < 20; i++) {
            MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {

                @Override
                public Message postProcessMessage(
                        Message message) throws AmqpException {
                    // 设置消息过期时间
                    message.getMessageProperties().setExpiration(delay);
                    return message;
                }

            };
            rabbitTemplate.convertAndSend(exchange, key, rs, messagePostProcessor);
        }

        return R.SUCCESS(v);
    }
}
