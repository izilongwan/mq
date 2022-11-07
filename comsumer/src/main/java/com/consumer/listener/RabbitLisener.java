package com.consumer.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.common.entity.R;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RabbitLisener {
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.1"), exchange = @Exchange(value = "direct"), key = {
            "1", "0" }))
    public void listener1(R<String> msg) {
        log.info("msg 1 ==> {}", msg);
        int o = 1 / 0;
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "direct.2"), exchange = @Exchange(value = "direct"), key = {
                    "2", "0" })
    })
    public void lisener2(R<String> msg, Channel c, Message m) throws Exception {
        long tag = m.getMessageProperties().getDeliveryTag();
        log.info("2 tag ==> {} ==> {}", tag, msg);

        // try {
        // // log.info("{}", c);
        // // log.info("{}", m);
        // c.basicAck(tag, false);
        // } catch (Exception e) {
        // c.basicNack(tag, false, false);
        // e.printStackTrace();
        // }
        Thread.sleep(1000 / 2);
        // c.basicQos(2);
        // c.basicConsume("direct.2", null);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "direct.2"), exchange = @Exchange(value = "direct"), key = {
                    "2", "0" })
    })
    public void lisener3(R<String> msg, Channel c, Message m) throws Exception {
        long tag = m.getMessageProperties().getDeliveryTag();
        log.info("3 tag ==> {} ==> {}", tag, msg);

        // try {
        // c.basicAck(tag, false);
        // } catch (Exception e) {
        // c.basicNack(tag, false, false);
        // e.printStackTrace();
        // }
        Thread.sleep(1000);
    }

    /**
     * 死信消息：
     * 消息过期且未被消费
     * 队列消息长度最大限制
     * 消息拒收且未返回原队列
     *
     * 延迟队列：死信队列 + ttl
     */
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "ttl.queue", arguments = {
                    @Argument(name = "x-dead-letter-exchange", value = "direct"),
                    @Argument(name = "x-dead-letter-routing-key", value = "1"),
                    @Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer"), // 消息过期时间
            }), exchange = @Exchange(value = "ttl.exchange"), key = {
                    "2", "0" })
    })
    public void lisener4(R<String> msg, Channel c, Message m) throws Exception {
        long tag = m.getMessageProperties().getDeliveryTag();
        log.info("22 tag ==> {} ==> {}", tag, msg);

        // try {
        // c.basicAck(tag, false);
        // } catch (Exception e) {
        // c.basicNack(tag, false, false);
        // e.printStackTrace();
        // }
        Thread.sleep(1000);
    }

    // @RabbitListener(bindings = @QueueBinding(value = @Queue(name =
    // "error.queue"), exchange = @Exchange(value = "error.exchange"), key = {
    // "1" }))
    // public void errorListener(R<String> msg) {
    // log.info("error ==> {}", msg);
    // }
}
