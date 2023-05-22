package com.peterjxl.rabbitmq.demo3;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.peterjxl.rabbitmq.util.SleepUtils;
import com.rabbitmq.client.Channel;

/**
 * 消息在手动应答时不丢失，放回队列重新消费
 */
public class Worker04 {
    // 队列名称
    private final static String task_queue_name = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C2消费者等待消息处理，处理时间较长（效率低）...");

        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(task_queue_name, autoAck, (consumerTag, message) -> {
            SleepUtils.sleep(30);
            System.out.println("接收到消息：" + new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {
            System.out.println("消息消费被中断");
        });
    }
}
