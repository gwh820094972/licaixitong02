package com.gwh.manager.service;

import com.gwh.entity.Administrator;
import com.gwh.manager.repositories.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {
    @Autowired
    private AdministratorRepository repository;
//    注册
//    public Administrator register(Administrator administrator){
//        Administrator result = repository.save(administrator);
//        return result;
//    }
    //登录
    public Administrator login(String username, String password){
        return  repository.findAdministratorByUsernameAndPassword(username,password);
    }

}
