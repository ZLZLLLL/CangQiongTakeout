package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 菜品存在套餐内的数量
     *
     * @param id
     * @return
     */
    Long selectById(Long id);

    /**
     * 新增套餐内菜品
     *
     * @param dish
     */
    void save(SetmealDish dish);

    /**
     * 套餐分页查询
     *
     * @return
     */
    Page<SetmealVO> PageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据套餐id查询基本信息
     *
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> selectBySetmealId(Long id);

    /**
     * 根据套餐id删除菜品
     *
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 向套餐菜品表中插入菜品数据
     *
     * @param list
     */
    void insert(List<SetmealDish> list);
}
