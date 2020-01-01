package com.chz.rocketmq.component;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PullConsumer {
    private static final String CONSUMER_GROUP = "PULL_CONSUMER_GROUP";// 消费订阅组

    private static final String nameSrvAddr = "211.67.19.87:9876;211.67.19.87:9877";
    // 随着消息的不断积累，offset会不断累计，因此将某些offset进行存储
    private  static Map<MessageQueue,Long> map = new HashMap<>();
    private static final int MAX_NUM = 16;//每次最多16个消息

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(CONSUMER_GROUP);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setNamesrvAddr(nameSrvAddr);
        consumer.start();
        Set<MessageQueue> queues = consumer.fetchSubscribeMessageQueues("Topic1");
        for(MessageQueue queue:queues){
            //long offset = consumer.fetchConsumeOffset(queue,true);//得到每一个相关联Queue起始的offset
            //System.out.println(queue.getQueueId()+" "+queue.getTopic()+" "+queue.getBrokerName()+"=> "+offset);
            for(;;){
                long _offset = map.get(queue)==null?0:map.get(queue);//得到queue后从map中去取
                PullResult pullResult = consumer.pullBlockIfNotFound(queue, null, _offset, MAX_NUM);
                //把下一次要读取的offset进行存储
                System.out.println("minOffset  "+pullResult.getMinOffset()+"  maxOffset  "+pullResult.getMaxOffset());
                long nextOffset = pullResult.getNextBeginOffset();
                System.out.println("nextBeginOffset  "+nextOffset);
                map.put(queue,nextOffset);
                List<MessageExt> msgList = pullResult.getMsgFoundList();
                if(null!=msgList){
                    for(MessageExt l:msgList){
                        System.out.println(l.getMsgId()+"  "+new String(l.getBody()));
                    }
                }
            }
        }
    }


}
