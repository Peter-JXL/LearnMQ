package com.peterjxl.rabbitmq.demo10Priority;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.*;

/**
 * 消费者：接收消息
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "priority_queue";

    // 接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        // 消费者接收消息
        // 消息接收失败后执行的方法
        channel.basicConsume(QUEUE_NAME, true, new DeliverCallback() {
            // 消息接收成功后执行的方法
            @Override
            public void handle(String consumerTag, Delivery message) {
                System.out.println("接收到消息：" + new String(message.getBody()));
            }
        }, consumerTag -> System.out.println("接收消息失败"));
    }
}
