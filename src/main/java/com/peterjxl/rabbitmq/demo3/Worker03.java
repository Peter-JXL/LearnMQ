package com.peterjxl.rabbitmq.demo3;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.peterjxl.rabbitmq.util.SleepUtils;
import com.rabbitmq.client.Channel;
/**
 * 消息在手动应答时不丢失，放回队列重新消费
 */
public class Worker03 {
    // 队列名称
    private final static String task_queue_name = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        System.out.println("C1消费者等待消息处理，处理时间较短（效率高）...");

        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(task_queue_name, autoAck, (consumerTag, message) -> {
            // 接收消息并处理
            System.out.println("接收到消息：" + new String(message.getBody()));
            // 休眠1秒
            SleepUtils.sleep(10);
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {
            System.out.println("消息消费被中断");
        });
    }
}
