package com.gwh.manager.controller;

import com.gwh.entity.Product;
import com.gwh.manager.param.ProductParam;
import com.gwh.manager.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    //    添加产品 /修改
    @PostMapping("")
    public Product addProduct(@RequestHeader String authId, @RequestHeader String sign,@RequestBody ProductParam productparam) {
        LOG.info("创建产品,参数:{}", productparam);

        Product product = new Product();
        BeanUtils.copyProperties(productparam,product);
        product = service.addProduct(product);
        LOG.info("创建产品,结果:{}", product);
        return product;
    }

    //按id删除产品
//    @DeleteMapping("/{id}")
//    public void deleteOne(@PathVariable String id) {
//        LOG.info("删除单个产品,id={}", id);
//        service.deleteOne(id);
//
//    }
    //按请求体中id删除产品
    @DeleteMapping("/{id}")
    public void deleteOne(@RequestHeader String authId, @RequestHeader String sign,@PathVariable String id) {
        LOG.info("删除单个产品,id={}", id);
        service.deleteOne(id);

    }

    //按id查找产品
    @GetMapping("/{id}")
    public Product findOne(@PathVariable String id) {
        LOG.info("查询单个产品,id={}", id);
        Product product = service.findOne(id);
        LOG.info("查询单个产品,结果={}", product);
        return product;
    }

    //按条件搜索产品
    @GetMapping("")
    public Page<Product> query(String ids,Integer lockTerm,BigDecimal minRewardRate, BigDecimal maxRewardRate, String status,
                               @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        LOG.info("查询产品,ids={},minRewardRate={},maxRewardRate={},status,pageNum={},pageSize={}");
        List<String> idList = null, statusList = null;
        if (!StringUtils.isEmpty(ids)) {
            idList = Arrays.asList(ids.split(","));
        }
        if (!StringUtils.isEmpty(status)) {
            statusList = Arrays.asList(status.split(","));
        }

        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<Product> page = service.query(idList,lockTerm, minRewardRate, maxRewardRate, statusList, pageable);
        LOG.info("查询产品,结果={}", page);
        return page;
    }
}
