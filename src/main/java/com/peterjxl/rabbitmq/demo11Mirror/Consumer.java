package com.peterjxl.rabbitmq.demo11Mirror;

import com.rabbitmq.client.*;

public class Consumer {
    public static final String QUEUE_NAME = "mirrior_hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.103");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("接收到消息：" + message);
        }, consumerTag -> {
            System.out.println("接收消息被中断");
        });
    }
}
