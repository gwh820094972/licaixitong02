package com.gwh.seller.controller;


import com.gwh.entity.User;
import com.gwh.seller.service.OrderService;
import com.gwh.seller.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private OrderService orderService;

    //注册
    @PostMapping("/register")
    public User regisiter(@RequestBody User user){
       user = service.register(user);
        return user;
    }

    //登录
    @PostMapping("/login")
    public User ogin(@RequestHeader String authId, @RequestHeader String sign,@RequestHeader  String username,  @RequestHeader  String password){
        return service.login(username,password);
    }

    //账户充值 根据用户id
    @PutMapping("/recharge")
    public User recharge(@RequestHeader String authId, @RequestHeader String sign,@RequestParam int id,@RequestParam String amount){

      return   service.recharge(id,amount);

    }
    //账户提现 根据用户id
    @PutMapping("/withdraw")
    public User withdraw(@RequestHeader String authId, @RequestHeader String sign,@RequestParam int id,@RequestParam String amount){

        return   service.withdraw(id,amount);

    }
    //账户赎回根据用户id
    @PutMapping("/redeem")
    public User redeem(@RequestHeader String authId, @RequestHeader String sign,@RequestParam int id,@RequestParam String amount){
        //金钱增加了，相当于充值对账户余额的操作
        return   service.redeem(id,amount);

    }

    //申购产品 根据用户id
    @PutMapping("/apply")
    public User apply(@RequestHeader String authId, @RequestHeader String sign,@RequestParam int id,@RequestParam String amount){
        //把钱买产品了，金钱减少，相当于提现对账户余额的操作
        return   service.apply(id,amount);

    }

}
