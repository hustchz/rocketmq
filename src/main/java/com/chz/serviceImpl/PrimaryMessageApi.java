package com.chz.serviceImpl;

import com.chz.enums.MessageStatus;
import com.chz.mapper.primary.PrimaryMessageMapper;
import com.chz.pojo.Message;
import com.chz.service.PrimaryMessageService;
import com.chz.utils.SnowFlakeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service(value="primaryMessageService")
@Transactional(value="primaryTransactionManager")
public class PrimaryMessageApi implements PrimaryMessageService {

    private static final String PRODUCERID = "6618121763815698432";

    private static final String CONSUMERID = "6618121763861835776";

    @Resource
    private PrimaryMessageMapper primaryMessageMapper;

    @Override
    public long updateMessageStatus(String messageId, MessageStatus messageStatus) throws Exception {
        Message message = primaryMessageMapper.selectByPrimaryKey(messageId);
        message.setStatus(messageStatus.name());
        primaryMessageMapper.updateByPrimaryKeySelective(message);
        return 0;
    }

    @Override
    public long insertMessage() throws Exception {
        // 添加一个新消息 这里模拟将消息的发送方和消费方ID指定
        SnowFlakeUtils snowFlakeUtils = new SnowFlakeUtils(2,4
                ,"2015-01-01","YYYY-MM-dd");
        long messageId = snowFlakeUtils.getNextId();
        Message message = new Message();
        message.setId(String.valueOf(messageId));
        message.setStatus(MessageStatus.NOT_SEND.name());
        message.setProducerid(PRODUCERID);
        message.setConsumerid(CONSUMERID);
        message.setCreatetime(new Date());
        return primaryMessageMapper.insert(message);
    }
}
