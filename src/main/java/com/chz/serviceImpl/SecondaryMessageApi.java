package com.chz.serviceImpl;

import com.chz.enums.MessageStatus;
import com.chz.mapper.secondary.SecondaryMessageMapper;
import com.chz.pojo.Message;
import com.chz.service.SecondaryMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service(value="secondaryMessageService")
@Transactional
public class SecondaryMessageApi implements SecondaryMessageService {

    private static final String PRODUCERID = "6618121763815698432";

    private static final String CONSUMERID = "6618121763861835776";

    @Resource
    private SecondaryMessageMapper secondaryMessageMapper;

    @Override
    public long updateMessageStatus(String messageId, MessageStatus messageStatus) throws Exception {
        Message message = secondaryMessageMapper.selectByPrimaryKey(messageId);
        message.setStatus(messageStatus.name());
        secondaryMessageMapper.updateByPrimaryKeySelective(message);
        return 0;
    }

    @Override
    public long insertSelective(Message message) throws Exception {
        return secondaryMessageMapper.insertSelective(message);
    }
}
