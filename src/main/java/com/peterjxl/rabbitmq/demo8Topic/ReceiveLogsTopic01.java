package com.peterjxl.rabbitmq.demo8Topic;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueDeclare("Q1", false, false, false, null);
        channel.queueBind("Q1", EXCHANGE_NAME, "*.orange.*");
        channel.basicConsume("Q1", true, (consumerTag, message) -> {
            System.out.println("接受队列：Q1 "
                    +  "绑定键： " + message.getEnvelope().getRoutingKey()
                    + " 收到消息： " + new String(message.getBody()));

        }, consumerTag -> {}
        );
    }
}
