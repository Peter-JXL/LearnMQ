package com.peterjxl.rabbitmq.demo10Priority;


import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import java.util.HashMap;
import java.util.Map;

/**
 * 生产者：发送消息
 */
public class Producer {

    // 队列名称
    public static final String QUEUE_NAME = "priority_queue";

    // 发送消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10); // 设置队列的最大优先级为10, 数字越大优先级越高, 不要设置太大, 会影响性能
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

        for (int i = 1; i < 11; i++) {
            String message = "Hello World" + i;
            if (i == 5) {   //设置第五条消息的优先级为5
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());

            }else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }

        System.out.println("发送消息完成");
    }
}
