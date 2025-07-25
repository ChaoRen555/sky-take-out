package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Get shop status
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("Shop getStatus status:{}", status == 1 ? "Open" : "Closed");
        return Result.success(status);
    }


}
