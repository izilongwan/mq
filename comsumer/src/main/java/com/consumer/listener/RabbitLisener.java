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

/**
 * 消息确认消费机制：
 * 开启生产者确认默认，确保消息到达队列
 * 开启持久化，确保未消费消息不会丢失
 * 开启消费者确认机制为AUTO，spring确认处理成功返回ack
 * 开启消费者失败重试机制，设置messageRecoverer，失败转发到异常交换机，特殊处理
 */
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
     * 死信消息：队列转发到交换机
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

    /**
     * 消息堆积：
     * 惰性队列（磁盘存储，消息上线高；读写速度受限于磁盘）
     * 增加消费者
     * 消费端开启多线程
     */
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "lazy.queue", arguments = {
                    @Argument(name = "x-queue-mode", value = "lazy")
            }), exchange = @Exchange(value = "lazy.exchange"), key = {
                    "0" })
    })
    public void lazyListener(R<String> msg) {
        log.info("lazy ==> {}", msg);
    }
}
