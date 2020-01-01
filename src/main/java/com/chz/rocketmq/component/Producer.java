package com.chz.rocketmq.component;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class Producer {
    private static final String nameSrvAddr = "211.67.19.87:9876;211.67.19.87:9877";
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer  = new DefaultMQProducer("PRODUCERS1");//指定生产组
        //producer.setInstanceName("Producer1");
        producer.setNamesrvAddr(nameSrvAddr);
        // 启动客户端，才能发送消息
        producer.start();
        // 发送消息
        for(int i=0;i<500;i++){
            String content = "RocketMQ-"+i;
            Message message = new Message("Topic1",content.getBytes("UTF-8"));
            // 可以创建延迟消息(1s 5s 10s 30s 1m 2m),一定时间后再把消息发送给broker
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    /***
                     *     SEND_OK, 没有发生下面三个问题就是完全ok的
                     *     FLUSH_DISK_TIMEOUT, 刷盘超时，当broker的策略设置成SYNC_FLUSH才会报这个
                     *     FLUSH_SLAVE_TIMEOUT, 主从同步超时，当broker的策略设置成SYNC_MASTER才会报这个
                     *     SLAVE_NOT_AVAILABLE 没有SLAVE而且是同步主从(broker的策略设置成SYNC_MASTER)
                     * **/
                    System.out.println("success   "+sendResult.getMsgId()+" 消息状态 "+sendResult.getSendStatus().name());
                }
                @Override
                public void onException(Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            },6000);
            Thread.sleep(2000);
        }
    }
}
