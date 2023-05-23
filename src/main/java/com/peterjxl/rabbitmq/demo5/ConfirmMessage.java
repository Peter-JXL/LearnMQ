package com.peterjxl.rabbitmq.demo5;

import com.peterjxl.rabbitmq.util.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {

    //批量发送消息的个数
    public static final int message_count = 1000;

    public static void main(String[] args) throws Exception{
        //publishMessageIndividually();      //发布1000个单独确认消息,耗时650ms
        // publishMessageBatcg();              //发布1000个批量确认消息,耗时481ms
        publishMessageAsync();              //发布1000个异步确认消息,耗时58ms
    }

    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        // 批量发消息
        for (int i = 0; i < message_count; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + message_count + "个单独确认消息,耗时" + (end - begin) + "ms");
    }

    public static void publishMessageBatcg() throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        // 批量确认的消息个数（不一定要1000个发完后再确认，可以每发100个就确认一次）
        int batchSize = 100;

        // 批量发消息
        for (int i = 1; i < message_count + 1; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            if(i % batchSize == 0){
                channel.waitForConfirms();
            }
        }


        long end = System.currentTimeMillis();
        System.out.println("发布" + message_count + "个单独确认消息,耗时" + (end - begin) + "ms");
    }

    // 异步发布确认
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序列号
         * 3.支持高并发（多线程）
         * 4.此处的map不需要设置容量，底层采用跳表实现
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>(); //outstanding:未解决的

        long begin = System.currentTimeMillis();

        // 消息确认成功的回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {

            System.out.println("确认的消息:" + deliveryTag);
            // 2：删除已确认的消息，剩下的就是未确认的消息
            // 判断是否批量，是则删除全部，不是则逐个删除
            if (multiple) {
                // 返回的是小于等于当前序号的未确认消息，因为是跳表，所以可以直接删除
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
        };

        // 消息确认失败的回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            // 3.打印未确认的消息
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息:" + message + " ,序号: " + deliveryTag);
        };

        channel.addConfirmListener(ackCallback,nackCallback);   // 异步确认

        for (int i = 0; i < message_count; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes());

            // 1:此处记录下所有要发送的消息，消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);    // getNextPublishSeqNo()：获取下一条消息的序号
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + message_count + "个异步确认消息,耗时" + (end - begin) + "ms");

    }
}
