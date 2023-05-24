package com.peterjxl.rabbitmq.demo9;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

public class Consumer01 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 要传入队列配置对象
        Map<String, Object> arguments = new HashMap<>();

        // 正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);

        // 正常队列设置死信routing-key
        arguments.put("x-dead-letter-routing-key", "lisi");

        // 设置正常队列的长度限制
        //arguments.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");


        channel.basicConsume(NORMAL_QUEUE, false, (consumerTag, message) -> {
            String msg = new String(message.getBody());
            if(msg.equals("info5")){
                System.out.println("Consumer01接收到消息：" + msg + "，此消息被拒绝");
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            }else {
                System.out.println("Consumer01接收到消息：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        }, consumerTag -> {});
    }
}
