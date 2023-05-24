package com.peterjxl.rabbitmq.demo8Topic;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

public class ReceiveLogsTopic02 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueDeclare("Q2", false, false, false, null);
        channel.queueBind("Q2", EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind("Q2", EXCHANGE_NAME, "lazy.#");
        channel.basicConsume("Q2", true, (consumerTag, message) -> {
            System.out.println("接受队列：Q2 "
                    +  "绑定键： " + message.getEnvelope().getRoutingKey()
                    + " 收到消息： " + new String(message.getBody()));

        }, consumerTag -> {}
        );
    }
}
