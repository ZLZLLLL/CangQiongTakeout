package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDishMapper {

    /**
     * 菜品存在套餐内的数量
     *
     * @param id
     * @return
     */
    Long selectById(Long id);
}
