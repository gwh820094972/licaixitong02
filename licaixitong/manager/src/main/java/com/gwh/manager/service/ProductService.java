package com.gwh.manager.service;

import com.gwh.entity.Product;
import com.gwh.entity.enums.ProductStatus;
import com.gwh.manager.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    private static Logger LOG = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository repository;

//    添加产品
    public Product addProduct(Product product) {
        LOG.debug("创建产品，参数:{}", product);
        // 数据校验
        checkProduct(product);

        //设置默认值
        setDefault(product);

        //保存
        Product result = repository.save(product);

        LOG.debug("创建产品, 结果:{}", result);
        return result;
    }

    /*
     * 产品数据校验
     * 1. 要为非空数据
     * 2. 收益率要在0%-30%以内
     * 3. 投资步长需为整数
     * @param product
     */
    private void checkProduct(Product product) {
        Assert.notNull(product.getId(), "编号不可为空");
        Assert.notNull(product.getName(), "名称不可为空");
        Assert.notNull(product.getThresholdAmount(), "起投金额不可为空");
        Assert.notNull(product.getStepAmount(), "投资步长不可为空");
        Assert.notNull(product.getLockTerm(), "锁定期不可为空");
        Assert.notNull(product.getRewardRate(), "收益率不可为空");
        Assert.notNull(product.getStatus(), "状态不可为空");
        Assert.isTrue(BigDecimal.ZERO.compareTo(product.getRewardRate()) < 0 && BigDecimal.valueOf(30).compareTo(product.getRewardRate()) >= 0, "收益率范围错误");
        Assert.isTrue(BigDecimal.valueOf(product.getStepAmount().longValue()).compareTo(product.getStepAmount()) == 0, "投资步长需为整数");

    }


    /**
     * 设置默认值
     * 创建时间、更新时间
     * 投资步长、锁定期、状态
     * @param product
     */
    private void setDefault(Product product) {
        if (product.getCreateAt() == null) {
            product.setCreateAt(new Date());
        }
        if (product.getUpdateAt() == null) {
            product.setUpdateAt(new Date());
        }
        if (product.getStepAmount() == null) {
            product.setStepAmount(BigDecimal.ZERO);
        }
        if (product.getLockTerm() == null) {
            product.setLockTerm(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.AUDITING.name());
        }
    }

    //删除产品
    public void deleteOne(String id){
        Assert.notNull(id, "需要产品编号参数");
        LOG.debug("删除单个产品,id={}", id);
        repository.deleteById(id);
        LOG.debug("删除单个产品,结果={}");
    }

    /**
     * 查询单个产品
     * @param id 产品编号
     * @return 返还对应产品或者null
     */
    public Product findOne(String id) {
        Assert.notNull(id, "需要产品编号参数");
        LOG.debug("查询单个产品，id={}", id);

        Product product = repository.findById(id).orElse(null);

        LOG.debug("查询单个产品,结果={}", product);

        return product;
    }

    /**
     * 分页查询产品
     *
     * @param idList
     * @param minRewardRate
     * @param maxRewardRate
     * @param statusList
     * @param pageable
     * @return
     */
    public Page<Product> query(List<String> idList,Integer lockTerm,
                               BigDecimal minRewardRate, BigDecimal maxRewardRate,
                               List<String> statusList,
                               Pageable pageable) {

        LOG.debug("查询产品,idList={},lockTerm={},minRewardRate={},maxRewardRate={},statusList={},pageable={}", idList,lockTerm, minRewardRate, maxRewardRate, statusList, pageable);
        Specification<Product> specification = new Specification<Product>() {

            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root:要查询的实体类的对象 root.get:获取对应的数据库属性列
                Expression<String> idCol = root.get("id");
                Expression<Integer> lockTermCol= root.get("lockTerm");
                Expression<BigDecimal> rewardRateCol = root.get("rewardRate");
                Expression<String> statusCol = root.get("status");
                List<Predicate> predicates = new ArrayList<>();
                if (idList != null && idList.size() > 0) {
                    //在实参idList中存在的id才添加到predicates集合列表中
                    predicates.add(idCol.in(idList));
                }//compareTo()前面的数大返回+1,后面的数大返回-1,相等返回0
                if (minRewardRate != null && BigDecimal.ZERO.compareTo(minRewardRate) < 0) {
                    predicates.add(criteriaBuilder.ge(rewardRateCol, minRewardRate));
                }
                if (maxRewardRate != null && BigDecimal.ZERO.compareTo(maxRewardRate) < 0) {
                    predicates.add(criteriaBuilder.le(rewardRateCol, maxRewardRate));
                }
                if (statusList != null && statusList.size() > 0) {
                    predicates.add(statusCol.in(statusList));

                }  if (lockTerm != null && lockTerm > 0) {
                    predicates.add(criteriaBuilder.le(lockTermCol, lockTerm));
                }

                query.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        };

        Page<Product> page = repository.findAll(specification, pageable);

        LOG.debug("查询产品,结果={}", page);
        return page;
    }
}
