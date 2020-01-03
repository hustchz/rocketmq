package com.chz.controller;

import com.chz.service.PrimaryAccountService;
import com.chz.service.PrimaryTransactionalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Resource
    private PrimaryTransactionalService primaryTransactionalService;

    /*跳转至支付页面*/
    @RequestMapping("/toPayPage")
    public String toPayPage(){
        return "pay";
    }
    /*支付成功*/
    @RequestMapping("/success")
    public String paySuccess() throws Exception {
        try{
            // 事务的结果在上层去捕获
            primaryTransactionalService.drawingMoneyAndRecordMessage(10.0);
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }
}
