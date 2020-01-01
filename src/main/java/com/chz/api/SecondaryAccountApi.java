package com.chz.api;

import com.chz.mapper.primary.PrimaryAccountMapper;
import com.chz.mapper.secondary.SecondaryAccountMapper;
import com.chz.pojo.Account;
import com.chz.service.SecondaryAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * secondary数据源中的账户接口具体实现
 * **/
@Service
public class SecondaryAccountApi implements SecondaryAccountService {

    private static final String USERID = "6618121763861835776";//直接给出用户ID,做模拟

    @Resource
    private SecondaryAccountMapper secondaryAccountMapper;

    @Override
    public long savingMoney(double saveMoney) throws Exception {
        //在这里做一个模拟，对特定用户的账户余额进行修改
        Account account = secondaryAccountMapper.selectByPrimaryKey(USERID);
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
        Account account = secondaryAccountMapper.selectByPrimaryKey(USERID);
        if(null==account){
            throw new Exception("没有这个用户");
        }
        Double surplus = account.getSurplus();//得到该账户的余额
        surplus -= drawingMoney;
        account.setSurplus(surplus);
        return secondaryAccountMapper.updateByPrimaryKeySelective(account);
    }
}
