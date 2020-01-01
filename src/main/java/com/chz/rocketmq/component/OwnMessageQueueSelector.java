package com.chz.rocketmq.component;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

// 自定义规则，将消息放入指定的MessageQueue中
public class OwnMessageQueueSelector implements MessageQueueSelector {
    @Override
    public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
        String key = arg.toString();
        if("key".equalsIgnoreCase(key)){
            // 放入第一个MessageQueue中
            return list.get(0);
        }
        return list.get(1);
    }
}
