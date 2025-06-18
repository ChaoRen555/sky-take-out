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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dish Manage
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * Create new dish
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("Create new dish {}", dishDTO);
        dishService.saveWithFlavors(dishDTO);
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
        return Result.success();
    }
}
