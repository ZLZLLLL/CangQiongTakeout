package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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

    @Autowired
    private SetmealDishMapper setmealDishMapper;



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

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //使用pageHelper插件
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<Dish> page = dishMapper.page(dishPageQueryDTO);
        long total = page.getTotal();
        List<Dish> result = page.getResult();

        return new PageResult(total, result);
    }

    /**
     * 菜品批量删除
     *
     * @param ids
     */
    @Transactional
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            //1.起售中的商品不能被删除
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                //起售中，不能被删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
            //2.被套餐关联的商品不能被删除
            Long count = setmealDishMapper.selectById(id);
            if (count>0){
                //被套餐关联的商品不能被删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
            //3.一次可以删除多个菜品
            dishMapper.delete(id);
            //4.删除菜品后，相关的口味也要被删除
            dishFlavorMapper.delete(id);
        }
    }

    /**
     * 起售停售菜品
     * @param id
     * @param status
     */
    public void StartOrStop(Long id, Integer status) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();

        dishMapper.update(dish);
    }

    /**
     * 更新菜品数据
     * @param dishDTO
     */
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
    }

    /**
     * 根据菜品id查询数据
     *
     * @param id
     * @return
     */
    public DishVO selectByDishId(Long id) {
        DishVO dishVO = new DishVO();
        Dish dish=dishMapper.selectByDishId(id);
        List<DishFlavor> flavors=dishMapper.selectByFlavorsId(id);
        BeanUtils.copyProperties(dish,dishVO);
        BeanUtils.copyProperties(flavors,dishVO);
        return dishVO;
    }


}
