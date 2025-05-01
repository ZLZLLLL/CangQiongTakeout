package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 统计一段时间内的营业额
     *
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        //当前集合用于存放begin到end范围内的日期
        List<LocalDate> dateList = new ArrayList<>();

        LocalDate current = begin;

        while (!current.isAfter(end)) {
            dateList.add(current);
            current = current.plusDays(1);
        }


        List<Double> turnoverList = new ArrayList();

        for (LocalDate date : dateList) {
            //查询日期对应的营业额数据,营业额是指状态为状态为已完成的
            LocalDateTime min = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime max = LocalDateTime.of(date, LocalTime.MAX);

            //sql: select sun(amount) from orders where order_time<max && order_time>min and status=5
            Map map = new HashMap();
            map.put("begin", min);
            map.put("end", max);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByMap(map);

            //为了防止当营业额为0的时候，查询出来为null，被传为string传入
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //接收统计时间
        ArrayList<LocalDate> dateList = new ArrayList<>();

        LocalDate current = begin;
        while (!current.isAfter(end)) {
            dateList.add(current);
            current = current.plusDays(1);
        }


        //根据时间查询新增用户
        List<Integer> newUserList = new ArrayList<>();
        //根据时间查询总用户
        List<Integer> totalList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime min = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime max = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("end", max);
            //查询总的用户数量
            Integer total = userMapper.getByCreateTime(map);
            //新增用户数量
            map.put("start", min);
            //select count(*) from user where create_time &gt min and create_time &gl max
            Integer userSum = userMapper.getByCreateTime(map);

            //非空判断，防止出现null时候拼接进入
            userSum = userSum == null ? 0 : userSum;

            totalList.add(total);
            newUserList.add(userSum);

        }

        //返回封装结果
        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalList, ","))
                .build();
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        //接收统计时间
        ArrayList<LocalDate> dateList = new ArrayList<>();

        LocalDate current = begin;
        while (!current.isAfter(end)) {
            dateList.add(current);
            current = current.plusDays(1);
        }

        //遍历集合


        //每日订单数
        ArrayList<Integer> orderCountList = new ArrayList<>();
        //每日有效订单数
        ArrayList<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            Map map = new HashMap();
            //查询范围期间每天的订单总数 select count(id) from order where create_time > xx and create_time < xx
            LocalDateTime min = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime max = LocalDateTime.of(date, LocalTime.MAX);


            map.put("max", max);
            map.put("min", min);
            Integer orderSum = orderMapper.getOrderSum(map);


            //查询范围期间每天的有效订单数
            map.put("status", Orders.COMPLETED);
            Integer orderCompletedSum = orderMapper.getOrderSum(map);


            //非空判断
            orderSum = orderSum == null ? 0 : orderSum;
            orderCompletedSum = orderCompletedSum == null ? 0 : orderCompletedSum;

            orderCountList.add(orderSum);
            validOrderCountList.add(orderCompletedSum);


        }

        //查询期间订单总数
        Integer orderSum = orderCountList.stream().reduce(Integer::sum).orElse(0);
        ;
        //查询有效订单数
        Integer orderCompleteSum = validOrderCountList.stream().reduce(Integer::sum).orElse(0);
        ;


        //校验完成率
        Double orderCompletionRate = 0.0;
        if (orderSum != null && orderSum != 0) {
            orderCompletionRate = orderCompleteSum.doubleValue() / orderSum;
        }


        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(orderSum)
                .validOrderCount(orderCompleteSum)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 指定时间销量排名前十的套餐菜品
     *
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO getSlesTop10(LocalDate begin, LocalDate end) {

        LocalDateTime min=LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime max=LocalDateTime.of(end,LocalTime.MAX);
        //查询销量前十的数据
        List<GoodsSalesDTO> list = orderMapper.getSlesTop10(min, max);

        List<String> names = list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = list.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(names,","))
                .numberList(StringUtils.join(numbers,","))
                .build();
    }
}
