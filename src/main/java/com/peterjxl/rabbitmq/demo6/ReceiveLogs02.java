package com.peterjxl.rabbitmq.demo6;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

public class ReceiveLogs02 {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 生成一个临时队列, 队列名称是随机的, 当消费者断开与队列的连接时, 队列自动删除,
        String queueName = channel.queueDeclare().getQueue();

        // 将临时队列绑定到交换机上, 参数3是routingKey, 交换机根据这个key将消息转发到指定的队列上
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("ReceiveLogs02等待接收消息,把接收到的消息打印在屏幕上......");

        // 接收消息, 参数2是自动确认, 参数3是消息的回调
        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("ReceiveLogs02接收到的消息: " + new String(message.getBody()));
        }, consumerTag -> {
        });
    }
}
