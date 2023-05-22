package com.peterjxl.rabbitmq.demo4;


import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

/**
 * 这是一个工作线程，用于处理消息（相当于之前的消费者）
 */
public class WorkerDemo4 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        channel.basicQos(1);
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        }, consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        });
    }
}
