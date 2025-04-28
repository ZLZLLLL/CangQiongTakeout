package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {


    /**
     * 插入订单
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 用于替换微信支付更新数据库状态的问题
     *
     * @param orderStatus
     * @param orderPaidStatus
     */
    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} " +
            "where number = #{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, String orderNumber);


    /**
     * 分页条件查询并按下单时间排序
     *
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 根据订单id主键查询订单信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 订单查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> select(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态查询数量
     *
     * @param status
     * @return
     */
    @Select("select count(*) from orders where status=#{status}")
    Integer countStatus(Integer status);


    /**
     * 根据订单状态与下单时间查找过期订单
     * @param status
     * @param timeout
     * @return
     */
    @Select("select * from orders where status=#{status} and order_time<#{timeout}")
    List<Orders> TimeoutOrderLT(Integer status, LocalDateTime timeout);


}
