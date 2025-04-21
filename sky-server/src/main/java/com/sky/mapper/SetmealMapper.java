package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void save(Setmeal setmeal);

    /**
     * 修改套餐状态
     * @param build
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal build);

    /**
     * 根据setmealId查询套餐信息
     * @param id
     * @return
     */
    @Select("select * from setmeal where id=#{id}")
    Setmeal selectBySetmealId(Long id);

    /**
     * 根据setmealId删除套餐数据
     * @param id
     */
    void deleteBySetmealId(Long id);
}
