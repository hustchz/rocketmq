package com.chz.serviceImpl;

import com.chz.mapper.primary.PrimaryAccountMapper;
import com.chz.pojo.Account;
import com.chz.service.PrimaryAccountService;
import com.chz.service.PrimaryMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * primary数据源中的账户接口具体实现 带事务
 * **/
@Service(value = "primaryAccountService")
@Transactional(value="primaryTransactionManager")
public class PrimaryAccountApi implements PrimaryAccountService {

    private static final String USERID = "6618121763815698432";//直接给出用户ID,做模拟

    @Resource
    private PrimaryAccountMapper primaryAccountMapper;

    @Resource
    private PrimaryMessageService primaryMessageService;

    @Override
    public long savingMoney(double saveMoney) throws Exception {
        //在这里做一个模拟，对特定用户的账户余额进行修改
        Account account = primaryAccountMapper.selectByPrimaryKey(USERID);
        if(null==account){
            throw new Exception("没有这个用户");
        }
        Double surplus = account.getSurplus();//得到该账户的余额
        surplus += saveMoney;
        account.setSurplus(surplus);
        return primaryAccountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    public long drawingMoney(double drawingMoney) throws Exception {
        //在这里做一个模拟，对特定用户的账户余额进行修改
        Account account = primaryAccountMapper.selectByPrimaryKey(USERID);
        if(null==account){
            throw new Exception("没有这个用户");
        }
        Double surplus = account.getSurplus();//得到该账户的余额
        surplus -= drawingMoney;
        account.setSurplus(surplus);
        return primaryAccountMapper.updateByPrimaryKeySelective(account);
    }
}
