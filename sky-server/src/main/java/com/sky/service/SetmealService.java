package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {


    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 修改套餐状态
     *
     * @param id
     */
    void startOrStop(Long id, Integer status);

    /**
     * 根据套餐id批量删除
     *
     * @param ids
     */
    void deleteBySetmealId(List<Long> ids);

    /**
     * 根据套餐id查询套餐信息
     *
     * @param id
     * @return
     */
    SetmealVO selectBySetmealId(Long id);

    /**
     * 更新套餐数据
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);


}
