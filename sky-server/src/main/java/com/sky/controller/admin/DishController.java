package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Dish Manage
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Create new dish
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("Create new dish {}", dishDTO);
        dishService.saveWithFlavors(dishDTO);

        //Clean cached data
        String key = "dish_"+ dishDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * Dish Pagination Query
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete dishes by ids
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("Delete dish {}", ids);
        dishService.deleteBatch(ids);

        //Clean all cached data with key starting with dish_
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    /**
     * Query dishes info by id
     */
    @GetMapping("/{id}")
    public Result<DishVO> findById(@PathVariable Long id) {
        log.info("Find Dish by id {}", id);
        DishVO dishVO = dishService.getByIdWithFlavors(id);
        return Result.success(dishVO);
    }

    /**
     * Edit dish info
     */
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("Update dish {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        //Clean all cached data with key starting with dish_
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }
}
