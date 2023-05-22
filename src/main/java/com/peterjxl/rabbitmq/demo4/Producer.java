package com.peterjxl.rabbitmq.demo4;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class Producer {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare("hello", true, false, false, null);
        channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, "Hello".getBytes());
    }
}
