package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    /**
     * 新增菜品及其口味数据
     *
     * @param dishDTO
     */
    //事务注解
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //1.向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);

        //获取新增id——主键返回
        Long dishId = dish.getId();

        //2.向口味表查询多条数据
        List<com.sky.entity.DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors == null || flavors.size() == 0) {
        } else {
            //遍历结合，插入dishId
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }


    }
}
