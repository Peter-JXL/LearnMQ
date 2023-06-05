package com.peterjxl.rabbitmq.demo12Federation;

import com.rabbitmq.client.*;

public class Consumer {

    public static final String QUEUE_NAME = "mirror_hello";
    public static final String EXCHANGE_NAME = "fed_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.103");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("node2_queue", true, false, false, null);
        channel.queueBind("node2_queue", EXCHANGE_NAME, "routeKey");
    }
}
