package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {


    @Autowired
    private SetmealService setmealService;


    /**
     * 新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐:{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }


    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询套餐")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐:{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 修改套餐状态
     *
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("修改套餐状态")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result startOrStop(Long id, @PathVariable Integer status) {
        log.info("修改套餐状态:id{} status:{}", id, status);
        setmealService.startOrStop(id, status);
        return Result.success();
    }


    /**
     * 根据套餐id批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据套餐id批量删除")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result deleteById(@RequestParam List<Long> ids) {
        log.info("根据套餐id批量删除:{}", ids);
        setmealService.deleteBySetmealId(ids);
        return Result.success();
    }


    /**
     * 根据套餐id查询套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据套餐id查询套餐信息")
    public Result<SetmealVO> SelectBySetmealId(@PathVariable Long id) {
        log.info("根据套餐id查询套餐信息:{}", id);
        SetmealVO setmealVO = setmealService.selectBySetmealId(id);
        return Result.success(setmealVO);
    }


    /**
     * 修改套餐数据
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐数据")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐数据:{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }
}
