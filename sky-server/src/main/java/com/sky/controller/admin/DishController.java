package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }


    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询");
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    //todo 注解：@RequestParam
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品的批量删除,{}", ids);
        dishService.delete(ids);
        return Result.success();
    }


    /**
     * 起售停售菜品
     *
     * @param id
     */
    @PostMapping("/status/{status}")
    @ApiOperation("起售停售菜品")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("起售停售菜品:{},{}", id, status);
        dishService.StartOrStop(id, status);
        return Result.success();
    }


    /**
     * 根据菜品id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据菜品id查询数据")
    public Result<DishVO> selectById(@PathVariable Long id) {
        log.info("根据菜品id查询数据：{}", id);
        //查询数据
        DishVO dishVO=dishService.selectByDishId(id);
        return Result.success(dishVO);
    }


    /**
     * 更新菜品数据
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新菜品数据")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("更新菜品数据:{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }
}
