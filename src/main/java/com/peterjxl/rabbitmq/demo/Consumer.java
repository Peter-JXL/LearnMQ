package com.peterjxl.rabbitmq.demo;

import com.rabbitmq.client.*;

/**
 * 消费者：接收消息
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 接收消息
    public static void main(String[] args) throws Exception {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("root123");

        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();

        //声明
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("接收到消息：" + message);
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("接收消息被中断");
        };

        /**
         * 消费者，消费消息
         * 第一个参数：队列名称
         * 第二个参数：是否自动确认，设置为true表示消息接收到自动向MQ回复接收到了，MQ则会将消息从队列中删除；设置为false则需要手动确认
         * 第三个参数：消费者未成功消费的回调函数，可以用Lambda
         * 第四个参数：消费者中断消费的回调函数，可以用Lambda
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
