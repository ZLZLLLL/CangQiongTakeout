package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询商品数据
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据id更新商品数量
     *
     * @param shoppingCart
     */
    @Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 将商品添加到购物车中
     *
     * @param shoppingCart
     */
    void save(ShoppingCart shoppingCart);


    /**
     * 根据用户id清空购物车数据
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id=#{userid}")
    void deleteByUserId(Long userId);

    /**
     * 根据菜品或套餐id删除商品
     */
    void deleteById(ShoppingCart shoppingCart);

}
