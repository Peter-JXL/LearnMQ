package com.peterjxl.rabbitmq.demo2;


import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 生产者 发送大量消息
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);


        // 从控制台中输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // 发送消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成：" + message);
        }
    }
}
