package com.chz.rocketmq.component;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.BrokerConfig;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

public class Consumer {

    private static final String nameSrvAddr = "211.67.19.87:9876;211.67.19.87:9877";

    public static void main(String[] args) throws MQClientException {
        //DefaultMQPushConsumer是由系统控制读取操作，收到消息后调用registerMessageListener()来处理
        // DefaultMQPullConsumer由使用者自主控制消息读取操作
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CONSUMER1");
        consumer.setNamesrvAddr(nameSrvAddr);
        consumer.setMessageModel(MessageModel.CLUSTERING);// 集群模式
        //程序第一次启动从消息队列头取数据
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅Topic, * 代表接受这个Topic下的所有 Tag 消息
        consumer.subscribe("Topic2","*");
        // 接受回调的信息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for(MessageExt l:list){
                    System.out.println(l.getQueueId()+"   "+l.getMsgId()+"  "+new String(l.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 消费端是先设置好消费端的属性，之后启动
        // 启动consumer
        consumer.start();
        System.out.println("消费者启动成功");
    }
}
