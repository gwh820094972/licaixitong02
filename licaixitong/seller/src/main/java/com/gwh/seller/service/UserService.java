package com.gwh.seller.service;

import com.gwh.entity.User;
import com.gwh.seller.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;
    //注册
    public User register(User user) {
        //判断是否用户名已经存在
        if(username(user)){
            //数据校验
            checkUser(user);
            //判断是否用户名已经存在

            setDefault(user);

            //入库
            User result = repository.saveAndFlush(user);
            return result;
        }else {
            System.out.println("户名已经存在");
            return null;
        }

    }
    //登录
    public User login(String username, String  password){
        return  repository.findUserByUsernameAndPassword(username,password);
    }

    //账户充值 根据用户id
    public User recharge(int id,String amount){

//      User  user = repository.findUserByUsername(username);
        User user = repository.findUserById(id);

        //尽量用字符串的形式初始化，金额为BigDecimal类型
        BigDecimal amountBigDecimal = new BigDecimal(amount);
        //BigDecimal加法
        BigDecimal addStr =user.getBalance().add(amountBigDecimal);
        user.setBalance(addStr);
        repository.saveAndFlush(user);

        return user;
    }

    //账户提现 根据用户id
    public User withdraw (int id,String amount){
        User user = repository.findUserById(id);

        //尽量用字符串的形式初始化，金额为BigDecimal类型
        BigDecimal amountBigDecimal = new BigDecimal(amount);

        BigDecimal UserBalance = user.getBalance();

        if(UserBalance.compareTo(amountBigDecimal)>=0){
            // a.compareTo(b) : a>b时返回1， a=b时返回0 ，a<b时返回-1
            // 判断当用户余额>=提现金额时才能进行提现操作
            BigDecimal subStr =UserBalance.subtract(amountBigDecimal);
            user.setBalance(subStr);
            repository.saveAndFlush(user);
            return user;
       }
        else {
            return null;
        }

    }

    //申购产品 根据用户id
    public User apply (int id,String amount){
        User user = repository.findUserById(id);

        //尽量用字符串的形式初始化，金额为BigDecimal类型
        BigDecimal amountBigDecimal = new BigDecimal(amount);

        BigDecimal UserBalance = user.getBalance();

        if(UserBalance.compareTo(amountBigDecimal)>=0){
            // a.compareTo(b) : a>b时返回1， a=b时返回0 ，a<b时返回-1
            // 判断当用户余额>=申购金额时才能进行申购操作
            BigDecimal subStr =UserBalance.subtract(amountBigDecimal);
            user.setBalance(subStr);
            repository.saveAndFlush(user);
            return user;
        }
        else {
            return null;
        }

    }

    //用户赎回 根据用户id
    public User redeem(int id,String amount){

//      User  user = repository.findUserByUsername(username);
        User user = repository.findUserById(id);

        //尽量用字符串的形式初始化，金额为BigDecimal类型
        BigDecimal amountBigDecimal = new BigDecimal(amount);
        //BigDecimal加法
        BigDecimal addStr =user.getBalance().add(amountBigDecimal);
        user.setBalance(addStr);
        repository.saveAndFlush(user);

        return user;
    }

    //判断用户名是否已经存在
    private Boolean username(User user){
        User result = repository.findUserByUsername(user.getUsername());
        if(result==null){
            //用户名不存在，可以注册
            return true;
        }else {
            //用户名已经存在，不可以注册
            return false;
        }

    }

    /*
     * 产品数据校验
     */
    private void checkUser(User user) {
        Assert.notNull(user.getUsername(), "用户名不可为空");
        Assert.notNull(user.getPassword(), "密码");
        Assert.notNull(user.getBankCardNum(), "绑定银行卡号不可为空");
        Assert.notNull(user.getEmail(), "邮箱");
    }

    //完善数据
    private void setDefault (User user){
        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }



    }

}
