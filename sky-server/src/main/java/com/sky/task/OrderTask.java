package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单定时任务类，处理
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理未支付超时订单
     */
    @Scheduled(cron = "0 * * * * ? ")//每分钟触发一次
    public void processTimeoutOrder() {
        log.info("定时处理超时订单:{}", LocalDateTime.now());

        //查询下订单中，未支付且   下单时间 < 当前时间-15min 的订单，修改其状态
        //@Select("select * from orders where status=1 and order_time<#{timeout}")
        //设置超时时间 plusMinutes为加多少分钟
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> list = orderMapper.TimeoutOrderLT(Orders.PENDING_PAYMENT, time);

        //将超时订单状态设置为已取消
        if (list != null && list.size() > 0) {
            for (Orders orders : list) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }


    @Scheduled(cron = "0 0 1 * * ? ")//每天凌晨一点将所有订单状态修改为已完成
    public void processDeliveryOrder() {
        log.info("设置自动检测超时未完成订单:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusHours(-1);//前一天的订单
        List<Orders> list = orderMapper.TimeoutOrderLT(Orders.DELIVERY_IN_PROGRESS, time);

        //将超时订单状态设置为已取消
        if (list != null && list.size() > 0) {
            for (Orders orders : list) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
