package com.chz.rocketmq.component;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * 自定义消息规则，将消息发送到指定的MessageQueue中
 * **/
public class OwnProvider {
    private static final String nameSrvAddr = "211.67.19.87:9876;211.67.19.87:9877";

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("OWN_PROVIDER");
        producer.setNamesrvAddr(nameSrvAddr);
        producer.setRetryTimesWhenSendFailed(5);
        producer.start();
        for(int i = 0;i < 100;i++){
            String content = "OWN_MESSAGE_" + i ;
            Message message = new Message("Topic2","TAG_A",content.getBytes("utf-8"));
            producer.send(message, new OwnMessageQueueSelector(), "key", new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult.getMsgId()+","+sendResult.getSendStatus().name());
                }

                @Override
                public void onException(Throwable throwable) {

                }
            },10000);
        }
    }

}
