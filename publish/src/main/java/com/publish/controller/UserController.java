package com.publish.controller;

import javax.annotation.Resource;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.common.entity.R;

@RestController
@RequestMapping("p")
public class UserController {
    @Resource
    RabbitTemplate rabbitTemplate;

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

    @Bean
    public Queue firstQueue() {
        return new Queue("simple.qu");
    }
}