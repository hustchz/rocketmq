package com.chz.serviceImpl;

import com.chz.service.PrimaryAccountService;
import com.chz.service.PrimaryMessageService;
import com.chz.service.PrimaryTransactionalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Random;

/**
 * primary 数据源 针对事务的接口实现
 * **/
@Service(value="primaryTransactionalService")
@Transactional(value="primaryTransactionManager")
public class PrimaryTransactionalApi implements PrimaryTransactionalService {

    @Resource
    private PrimaryMessageService primaryMessageService;

    @Resource
    private PrimaryAccountService primaryAccountService;

    private Random random = new Random();

    @Override
    public long drawingMoneyAndRecordMessage(double drawingMoney) throws Exception {
        // 将扣钱和消息落地放在一个事务中
        primaryMessageService.insertMessage();
        long updateRow = 0;
        updateRow = primaryAccountService.drawingMoney(drawingMoney);
        if(random.nextInt(10) < 6){
            throw new RuntimeException("模拟账户余额扣减失败");
        }
        return  updateRow;
    }
}
