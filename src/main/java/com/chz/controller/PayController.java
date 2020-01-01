package com.chz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pay")
public class PayController {
    // 跳转至支付页面
    @RequestMapping("/toPayPage")
    public String toPayPage(){
        return "pay";
    }
    // 支付成功
    @RequestMapping("/success")
    public String paySuccess(){
        return "success";
    }
}
