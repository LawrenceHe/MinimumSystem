package com.example.demo.dao;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select("SELECT id,username,password,enabled" +
            " FROM users WHERE id = #{id}")
    User getUserById(Long id);

    @Select("SELECT id,username,password,enabled" +
            " FROM users WHERE username = #{username}")
    User getByUsername(String username);

    @Select("SELECT id,mobile,enabled" +
            " FROM users WHERE mobile = #{mobile}")
    User getByMobile(String mobile);

    @Options(useGeneratedKeys = true)
    @Insert("insert into users(id,username,password,mobile,enabled,created_by,updated_by)" +
            " values(#{id}, #{username},#{password},#{mobile},#{enabled},'system','system')")
    long insertUser(User user);

}