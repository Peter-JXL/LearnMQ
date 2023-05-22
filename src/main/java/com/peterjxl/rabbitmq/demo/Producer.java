package com.peterjxl.rabbitmq.demo;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者：发送消息
 */
public class Producer {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发送消息
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

        /**
         * 生成一个队列
         * 第一个参数：队列名
         * 第二个参数：是否持久化，默认false，表示保存在内存中（不持久化）
         * 第三个参数：是否独占队列，默认false，表示不独占队列（消息共享），true则表示只供一个消费者消费
         * 第四个参数：最后一个消费者断开连接后，是否自动删除队列，默认false，表示不自动删除
         * 第五个参数：队列的其他参数，如：存活时间
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 定义要发送的消息
        String message = "Hello World";
        /**
         * 发送一个消息
         * 第一个参数：交换机名称，如果没有则指定空字符串（表示使用默认的交换机）
         * 第二个参数：路由key，简单模式中可以使用队列名称
         * 第三个参数：消息其他属性
         * 第四个参数：消息内容
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("发送消息完成");
    }
}
