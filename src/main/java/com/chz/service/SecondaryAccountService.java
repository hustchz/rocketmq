package com.chz.service;

/**
 * 针对secondary数据源的账户业务操作
 * **/
public interface SecondaryAccountService {

    long savingMoney(double saveMoney)throws Exception;//存款接口

    long drawingMoney(double drawingMoney)throws Exception;//取款接口
}
