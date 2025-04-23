package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String Key="Shop_Status";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 设置店铺营业状态
     *
     * @param status
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public void setShopStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态:{}", status == 1 ? "营业中" : "打烊中");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(Key, status);
    }


    /**
     * 获取店铺营业状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Long> getShopStatus() {
        log.info("获取店铺营业状态");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long shopStatus = (Long) valueOperations.get(Key);
        return Result.success(shopStatus);
    }

}
