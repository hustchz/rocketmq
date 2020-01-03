package com.chz.service;

import com.chz.enums.MessageStatus;
import com.chz.pojo.Message;

/**
 * secondary 数据源的消息表操作
 * **/
public interface SecondaryMessageService {
    /*更新消息的状态*/
    long updateMessageStatus(String messageId, MessageStatus messageStatus)throws Exception;
    /* 新增加一个message */
    long insertSelective(Message message)throws Exception;
}
