package com.peterjxl.rabbitmq.demo9;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public class ProducerDemo9 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        // 设置消息的TTL时间, 单位是ms
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        // 发送死信消息，设置TTL
        channel.basicPublish(NORMAL_EXCHANGE, "lisi", null, "这是一条消息".getBytes());
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes());
        }
    }
}
