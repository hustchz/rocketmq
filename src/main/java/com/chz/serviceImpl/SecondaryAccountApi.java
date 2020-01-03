package com.chz.serviceImpl;

import com.chz.mapper.secondary.SecondaryAccountMapper;
import com.chz.pojo.Account;
import com.chz.service.SecondaryAccountService;
import com.chz.service.SecondaryMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * secondary数据源中的账户接口具体实现
 * **/
@Service(value = "secondaryAccountService")
public class SecondaryAccountApi implements SecondaryAccountService {

    private static final String USERID = "6618121763861835776";//直接给出用户ID,做模拟

    @Resource
    private SecondaryAccountMapper secondaryAccountMapper;

    @Resource
    private SecondaryMessageService secondaryMessageService;

    @Transactional(value = "secondaryTransactionManager")
    @Override
    public long updateByPrimaryKeySelective(Account account) throws Exception {
        return secondaryAccountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    public long saveMoney(double saveMoney) throws Exception {
        //在这里做一个模拟，对特定用户的账户余额进行修改
        Account account = selectByPrimaryKey(USERID);
        if(null==account){
            throw new Exception("没有这个用户");
        }
        Double surplus = account.getSurplus();//得到该账户的余额
        surplus += saveMoney;
        account.setSurplus(surplus);
        return secondaryAccountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    public long drawingMoney(double drawingMoney) throws Exception {
        //在这里做一个模拟，对特定用户的账户余额进行修改
        Account account = selectByPrimaryKey(USERID);
        if(null==account){
            throw new Exception("没有这个用户");
        }
        Double surplus = account.getSurplus();//得到该账户的余额
        surplus -= drawingMoney;
        account.setSurplus(surplus);
        return secondaryAccountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    public Account selectByPrimaryKey(String id) throws Exception {
        Account account = secondaryAccountMapper.selectByPrimaryKey(USERID);
        return account;
    }
}
