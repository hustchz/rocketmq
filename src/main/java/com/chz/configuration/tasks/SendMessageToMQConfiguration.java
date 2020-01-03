package com.chz.configuration.tasks;

import com.chz.abstractApi.AbstractMessageEncodeAndDecode;
import com.chz.abstractImpl.ByteArrayMessageEncodeAndDecode;
import com.chz.enums.MessageStatus;
import com.chz.mapper.primary.PrimaryMessageMapper;
import com.chz.pojo.Message;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 *  将消息表中的未消费的消息投递到MQ
 * **/
@Configuration
@EnableScheduling //开启定时任务
public class SendMessageToMQConfiguration implements SchedulingConfigurer {

    @Resource
    private PrimaryMessageMapper primaryMessageMapper;

    @Value("${task1.cron}")
    private String cron;

    @Value("${task1.failRetryMaxNum}")
    private int failRetryMaxNum;

    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Value("${rocketmq.topic}")
    private String topic;

    @Value("${rocketmq.tag}")
    private String tag;

    private Logger logger = Logger.getLogger(SendMessageToMQConfiguration.class);

    private final long maxSleepTime = 2000L;//当没有待消费的消息时最长休眠时间

    private int sleepCount;//连续没有待消费的消息次数

    /* 定义编解码方式*/
    private AbstractMessageEncodeAndDecode abstractMessageEncodeAndDecode
            = new ByteArrayMessageEncodeAndDecode();

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addCronTask(new Runnable() {
            @Override
            public void run() {
                List<Message> notConsumeMessages = primaryMessageMapper.selectAllNotSend(100);
                if(notConsumeMessages.size()==0){
                    // 暂时没有消息
                    logger.info("暂时没有待发送的消息");
                    try {
                        long sleepTime = (++sleepCount) * 500L;
                        sleepTime = sleepTime > maxSleepTime ? maxSleepTime:sleepTime;
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{

                    for(Message _message:notConsumeMessages){
                        org.apache.rocketmq.common.message.Message mess =
                                null;
                        try {
                            mess = new org.apache.rocketmq.common.message.Message(topic,tag,abstractMessageEncodeAndDecode.encode(_message));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            defaultMQProducer.send(mess, new SendCallback() {
                                @Override
                                public void onSuccess(SendResult sendResult) {
                                    System.out.println(sendResult.getMsgId()+" "+sendResult.getSendStatus().name());
                                    _message.setStatus(MessageStatus.SUCCESS_SEND.name());
                                    primaryMessageMapper.updateByPrimaryKey(_message);
                                }

                                @Override
                                public void onException(Throwable throwable) {
                                    throwable.getStackTrace();
                                }
                            },10000);
                        } catch (MQClientException e) {
                            e.printStackTrace();
                        } catch (RemotingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sleepCount = 0;
                }
                // 将未消费的消息投递到MQ中
            }
        },cron);
    }
}
