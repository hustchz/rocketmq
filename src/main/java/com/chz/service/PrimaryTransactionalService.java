package com.chz.service;

import com.chz.pojo.Message;

/**
 *  针对Primary 数据源的事务接口
 * **/
public interface PrimaryTransactionalService {

    // 扣款和写消息表在一个事务中
    public long drawingMoneyAndRecordMessage(double drawingMoney)throws Exception;
}
