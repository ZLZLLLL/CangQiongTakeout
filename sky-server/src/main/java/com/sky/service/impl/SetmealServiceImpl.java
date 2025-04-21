package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        //向套餐表中插入基础数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.save(setmeal);

        //获取主键返回数据
        Long setmealId = setmeal.getId();

        //向套餐菜品表中插入菜品关联数据
        List<SetmealDish> setmealDish = setmealDTO.getSetmealDishes();
        if (setmealDish.size()!=0 && setmealDish!=null){
            //遍历菜品内容，并插入套餐id
           setmealDish.forEach(SetmealDish-> SetmealDish.setSetmealId(setmealId));
            for (SetmealDish dish : setmealDish) {
                setmealDishMapper.save(dish);
            }
        }
    }

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealDishMapper.PageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改套餐状态
     *
     * @param id
     */
    public void startOrStop(Long id, Integer status) {
        Setmeal build = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(build);
    }


    /**
     * 根据套餐id批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteBySetmealId(List<Long> ids) {
        for (Long id : ids) {
            //查询状态
            Setmeal setmeal = setmealMapper.selectBySetmealId(id);

            //起售中的套餐不能被删除
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            } else {
                setmealMapper.deleteBySetmealId(id);
            }
        }
    }

    /**
     * 根据套餐id查询套餐信息
     *
     * @param id
     * @return
     */
    public SetmealVO selectBySetmealId(Long id) {
        //查询套餐基础信息
        Setmeal setmeal = setmealMapper.selectBySetmealId(id);
        //查询套餐相关联的菜品信息
        List<SetmealDish> list = setmealDishMapper.selectBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(list);
        return setmealVO;
    }

    /**
     * 更新套餐数据
     *
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //更新套餐基本数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);

        //更新套餐菜品表中的对应数据,先将套餐对应的菜品表中的数据进行删除
        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);
        //重新插入
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        if (list.size()!=0){
            for (SetmealDish setmealDish : list) {
                setmealDish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insert(list);
        }
    }


}
