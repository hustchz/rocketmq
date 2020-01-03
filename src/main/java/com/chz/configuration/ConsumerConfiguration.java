package com.chz.configuration;

import com.alibaba.fastjson.JSONException;
import com.chz.abstractApi.AbstractMessageEncodeAndDecode;
import com.chz.abstractImpl.ByteArrayMessageEncodeAndDecode;
import com.chz.mapper.secondary.SecondaryMessageMapper;
import com.chz.pojo.Message;
import com.chz.service.SecondaryTransactionalService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 读取rocketMQ配置文件 消费端
 * **/
@Configuration
public class ConsumerConfiguration {

    @Value("${rocketmq.consumer.groupName}")
    private String groupName;

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.consumer.instanceName}")
    private String instanceName;

    @Value("${rocketmq.consumer.pullInterval}")
    private int pullInterval;

    @Value("${rocketmq.consumer.pullThresholdForQueue}")
    private int pullThresholdForQueue;

    @Value("${rocketmq.consumer.consumeConcurrentlyMaxSpan}")
    private int consumeConcurrentlyMaxSpan;

    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    @Value("${rocketmq.topic}")
    private String topic;

    @Value("${rocketmq.tag}")
    private String tag;

    @Resource
    private SecondaryTransactionalService secondaryTransactionalService;

    @Resource
    private SecondaryMessageMapper secondaryMessageMapper;

    /* 定义编解码方式*/
    private AbstractMessageEncodeAndDecode abstractMessageEncodeAndDecode
            = new ByteArrayMessageEncodeAndDecode();

    @Bean(name="payMQPushConsumer")
    public DefaultMQPushConsumer getRocketMQConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.groupName);
        consumer.setNamesrvAddr(this.namesrvAddr);
        consumer.setInstanceName(this.instanceName);
        consumer.setMessageModel(MessageModel.CLUSTERING);  //集群模式
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeConcurrentlyMaxSpan(consumeConcurrentlyMaxSpan);
        consumer.setPullThresholdForQueue(pullThresholdForQueue);
        consumer.setPullInterval(pullInterval);                //消息拉取时间间隔，默认为0，即拉完一次立马拉第二次，单位毫秒
        try {
            consumer.subscribe(this.topic, tag);
            consumer.registerMessageListener((MessageListenerConcurrently) (msgList, consumeConcurrentlyContext) -> {
                try {
                    MessageExt msg = null;
                    for (MessageExt aMsgList : msgList) {
                        System.out.println("开始消费");
                        msg = aMsgList;
                        // 得到消息进行反序列化
                        Message _message = (Message)abstractMessageEncodeAndDecode.decode(msg.getBody());
                        String messageId = _message.getId();
                        Message _msg = secondaryMessageMapper.selectByPrimaryKey(messageId);
                        if(null == _msg){
                            // 开始给另一个账户增加记录,在一个事务中
                            try {
                                secondaryTransactionalService.saveMoneyAndRecordMessage(_message,10);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return ConsumeConcurrentlyStatus.RECONSUME_LATER;//再投递重消费
                            }
                        }else{
                           // 说明是重复消息
                           System.out.println("不消费重复消息");
                        }
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }
}
