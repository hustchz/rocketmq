package com.chz.service;

import com.chz.enums.MessageStatus;

/**
 * secondary 数据源的消息表操作
 * **/
public interface SecondaryMessageService {

    long updateMessageStatus(String messageId, MessageStatus messageStatus)throws Exception;//更新消息的状态

    // 新增加一个message
    long insertMessage()throws Exception;
}
