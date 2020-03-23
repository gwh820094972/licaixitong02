package com.gwh.manager.service;


import com.gwh.entity.BankCard;
import com.gwh.manager.repositories.BankCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Service
public class BankCardService {

    @Autowired
    BankCardRepository bankCardRepository;
    //用户充值
    public String UserRecharge (String CardNum,String CardPassword,String amount){
       BankCard result = bankCardRepository.findBankByBankCardNumAndPassword(CardNum,CardPassword);
              Assert.notNull(result,"银行卡密码错误");

        //尽量用字符串的形式初始化，金额为BigDecimal类型
        BigDecimal amountBigDecimal = new BigDecimal(amount);

          BigDecimal CardBalance =  result.getBalance();
        if(CardBalance.compareTo(amountBigDecimal)>=0){
            // a.compareTo(b) : a>b时返回1， a=b时返回0 ，a<b时返回-1
            // 判断当银行卡余额>=充值金额时才能进行申购操作
            BigDecimal subStr =CardBalance.subtract(amountBigDecimal);
            result.setBalance(subStr);
            bankCardRepository.saveAndFlush(result);
            return result.getBalance().toString();
        } else {
            return null;
        }
    }

    //用户提现，增加银行卡金额
    public String  userWithdraw (String CardNum,String amount){
        BankCard result = bankCardRepository.findBankByBankCardNum(CardNum);
        Assert.notNull(result,"银行卡不能为空");
        //尽量用字符串的形式初始化，金额为BigDecimal类型
        BigDecimal amountBigDecimal = new BigDecimal(amount);

        BigDecimal cardBalance =  result.getBalance();
        BigDecimal addStr =cardBalance.add(amountBigDecimal);
        result.setBalance(addStr);
        bankCardRepository.saveAndFlush(result);

        return result.getBalance().toString();
    }
}
