package com.chz.serviceImpl;

import com.chz.enums.MessageStatus;
import com.chz.pojo.Account;
import com.chz.pojo.Message;
import com.chz.service.SecondaryAccountService;
import com.chz.service.SecondaryMessageService;
import com.chz.service.SecondaryTransactionalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Random;

/**
 * secondary 数据源 针对事务的接口实现
 * **/
@Service(value="secondaryTransactionalService")
@Transactional(value="secondaryTransactionManager")
public class SecondaryTransactionalApi implements SecondaryTransactionalService {

    @Resource
    private SecondaryMessageService secondaryMessageService;

    @Resource
    private SecondaryAccountService secondaryAccountService;

    private Random random = new Random();

    @Override
    public long saveMoneyAndRecordMessage(Message message, double saveMoney) throws Exception {
        //模拟账户修改异常,只有unchecked才会回滚，不用try-catch 虚拟机自动捕捉的异常
        Account account = secondaryAccountService.selectByPrimaryKey(message.getConsumerid());
        if(null==account){
            throw new Exception("没有这个用户");
        }
        Double surplus = account.getSurplus();
        surplus += saveMoney;
        account.setSurplus(surplus);
        // 消息表消息的插入 和 账户的更新在一个事务中
        message.setStatus(MessageStatus.SUCCESS_CONSUME.name());
        secondaryMessageService.insertSelective(message);
        long updateRow = 0;
        updateRow  = secondaryAccountService.updateByPrimaryKeySelective(account);
        // 模拟出错
        if(random.nextInt(10) < 6){
            throw new RuntimeException("模拟账户余额增加失败");
        }
        return updateRow;
    }
}
