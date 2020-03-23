package com.gwh.seller.controller;


import com.gwh.entity.User;
import com.gwh.seller.service.OrderService;
import com.gwh.seller.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.PublicKey;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private OrderService orderService;

    //注册 返回token
    @PostMapping("/register")
    public String register(@RequestBody User user){

        return service.register(user);
    }
    //用户修改用户信息modify 不能修改id、username
    @PostMapping("/modify")
    public String modify(@RequestHeader String authId, @RequestHeader String sign,@RequestParam String token,@RequestBody User user){
        return service.modify(user,token);
    }

    //登录 返回token
    @PostMapping("/login")
    public String login(@RequestHeader String authId, @RequestHeader String sign,@RequestHeader  String username,  @RequestHeader  String password){
        return service.login(username,password);
    }
    //获取用户信息
    @GetMapping("/userInfo")
    public User userInfo(@RequestParam String token){
        return service.userInfo(token);
    }

    //账户充值 根据用户id
    @PutMapping("/recharge")
    public User recharge(@RequestHeader String authId, @RequestHeader String sign,@RequestParam String token,@RequestParam String amount){

      return   service.recharge(token,amount);

    }
    //账户提现 根据用户id
    @PutMapping("/withdraw")
    public User withdraw(@RequestHeader String authId, @RequestHeader String sign,@RequestParam String token,@RequestParam String amount){

        return   service.withdraw(token,amount);

    }
    //账户赎回根据用户id
    @PutMapping("/redeem")
    public User redeem(@RequestHeader String authId, @RequestHeader String sign,@RequestParam String token,@RequestParam String amount){
        //金钱增加了，相当于充值对账户余额的操作
        return   service.redeem(token,amount);

    }

    //申购产品 根据用户id
    @PutMapping("/apply")
    public User apply(@RequestHeader String authId, @RequestHeader String sign,@RequestParam String token,@RequestParam String amount){
        //把钱买产品了，金钱减少，相当于提现对账户余额的操作
        return   service.apply(token,amount);

    }

}
