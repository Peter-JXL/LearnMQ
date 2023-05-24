package com.peterjxl.rabbitmq.demo9;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

public class Consumer02 {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        channel.basicConsume(DEAD_QUEUE, true, (consumerTag, message) -> {
            System.out.println("Consumer02接收到消息：" + new String(message.getBody()));
        }, consumerTag -> {});
    }
}
