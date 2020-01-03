package com.chz.service;

import com.chz.pojo.Message;

/**
 * 针对primary数据源的账户业务操作
 * **/
public interface PrimaryAccountService {

    long savingMoney(double saveMoney)throws Exception;//存款接口

    long drawingMoney(double drawingMoney)throws Exception;//取款接口

}
