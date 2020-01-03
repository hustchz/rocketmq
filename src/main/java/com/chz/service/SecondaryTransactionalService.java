package com.chz.service;

import com.chz.pojo.Message;

/**
 * secondary 数据源的事务接口
 * **/
public interface SecondaryTransactionalService {

    public long saveMoneyAndRecordMessage(Message message, double saveMoney) throws Exception;
}
