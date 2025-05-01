package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     *
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * 添加新用户
     *
     * @param user
     */
    void insert(User user);


    /**
     * 查询用户
     * @param userId
     * @return
     */
    @Select("select * from user where openid=#{userId}")
    User getById(Long userId);

    /**
     * 查询新增用户
     * @param map
     * @return
     */
    Integer getByCreateTime(Map map);


}
