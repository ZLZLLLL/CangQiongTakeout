package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String Key="Shop_Status";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 用户端查询店铺状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("用户端查询店铺状态")
    public Result<Long> getShopStatus() {
        log.info("用户端查询店铺状态");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long shopStatus = (Long) valueOperations.get(Key);
        return Result.success(shopStatus);
    }
}
