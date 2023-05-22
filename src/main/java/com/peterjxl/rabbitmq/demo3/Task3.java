package com.peterjxl.rabbitmq.demo3;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 消息在手动应答时不丢失，放回队列重新消费
 */
public class Task3 {

    // 队列名称
    private final static String task_queue_name = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        channel.queueDeclare(task_queue_name, false, false, false, null);
        // 从控制台接收消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", task_queue_name, null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
