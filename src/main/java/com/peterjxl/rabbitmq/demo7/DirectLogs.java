package com.peterjxl.rabbitmq.demo7;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

public class DirectLogs {


    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        channel.basicPublish(EXCHANGE_NAME,"info",null,"info message".getBytes());
        channel.basicPublish(EXCHANGE_NAME,"warning",null,"warning message".getBytes());
        channel.basicPublish(EXCHANGE_NAME,"error",null,"error message".getBytes());
    }
}
