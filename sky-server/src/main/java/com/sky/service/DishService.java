package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {


    /**
     * 新增菜品和对应的方法
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 菜品批量删除
     *
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 起售停售菜品
     *
     * @param id
     */
    void StartOrStop(Long id, Integer status);


    /**
     * 根据菜品id查询数据
     *
     * @param id
     * @return
     */
    DishVO selectByDishId(Long id);


    /**
     * 更新菜品数据
     * @param dishDTO
     */
    void update(DishDTO dishDTO);
}
