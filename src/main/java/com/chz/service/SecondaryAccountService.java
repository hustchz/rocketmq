package com.chz.service;

import com.chz.pojo.Account;
import com.chz.pojo.Message;

/**
 * 针对secondary数据源的账户业务操作
 * **/
public interface SecondaryAccountService {

    long updateByPrimaryKeySelective(Account account)throws Exception;//更新账户信息

    long saveMoney(double saveMoney)throws Exception;//存款接口

    long drawingMoney(double drawingMoney)throws Exception;//取款接口

    Account selectByPrimaryKey(String id)throws Exception;// 根据账户ID找到相应账户
}
