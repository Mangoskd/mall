package com.mango.mall.product;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mango.mall.product.entity.BrandEntity;
import com.mango.mall.product.service.BrandService;
import com.mango.mall.product.service.CategoryService;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallProductApplication.class)
class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Resource
    RedissonClient redissonClient;

    @Value("${spring.redis.host}")
    private String address;
    @Value("${spring.redis.password}")
    private String password;


}
