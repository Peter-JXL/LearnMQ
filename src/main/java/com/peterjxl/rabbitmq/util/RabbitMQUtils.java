package com.peterjxl.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 此类为连接RabbitMQ的工具类
 */
public class RabbitMQUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("root123");
        return factory.newConnection().createChannel();
    }
}
