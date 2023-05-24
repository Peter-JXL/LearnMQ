package com.peterjxl.rabbitmq.demo8Topic;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        Map<String, String > bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "被队列Q1，Q2接收到");
        bindingKeyMap.put("lazy.orange.elephant", "被队列Q1，Q2接收到");
        bindingKeyMap.put("quick.orange.fox", "被队列Q1接收到");
        bindingKeyMap.put("lazy.brown.fox", "被队列Q2接收到");
        bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定，但只被队列Q2接收一次");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何绑定，不会被任何队列接收到，会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit", "是四个单词，不匹配任何绑定，会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是四个单词，但匹配Q2");
        bindingKeyMap.forEach((bindingKey, message) -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, bindingKey, null, message.getBytes());
                System.out.println("生产者发出消息：" + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
