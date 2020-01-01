package com.chz.controller;

import com.chz.mapper.primary.PrimaryAccountMapper;
import com.chz.mapper.secondary.SecondaryAccountMapper;
import com.chz.pojo.Account;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试 多数据源配置的controller
 * **/
@RestController
public class MultiDataSourceController {
    @Resource
    private PrimaryAccountMapper primaryAccountMapper;
    @Resource
    private SecondaryAccountMapper secondaryAccountMapper;

    private static Account account;

    static{
        account = new Account();
        account.setId("222222222");
        account.setName("123");
        account.setSurplus(25.2);
        account.setRemaining(0.01);
    }
    @RequestMapping("/ds1")
    public String datasource1(){
        primaryAccountMapper.insertSelective(account);
        return "1";
    }
    @RequestMapping("/ds2")
    public String datasource2(){
        secondaryAccountMapper.insertSelective(account);
        return "2";
    }
}
