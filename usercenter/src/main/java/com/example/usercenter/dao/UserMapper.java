package com.example.usercenter.dao;

import com.example.common.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select("SELECT id,username,password,enabled," +
            "account_non_expired,credentials_non_expired,account_non_locked" +
            " FROM users WHERE id = #{id}")
    User getUserById(Long id);

    @Select("SELECT id,username,password,enabled," +
            "account_non_expired,credentials_non_expired,account_non_locked" +
            " FROM users WHERE username = #{username}")
    @Results(value = {
            @Result(property = "accountNonExpired", column = "account_non_expired"),
            @Result(property = "credentialsNonExpired", column = "credentials_non_expired"),
            @Result(property = "accountNonLocked", column = "account_non_locked")})
    User getByUsername(String username);

    @Options(useGeneratedKeys = true)
    @Insert("insert into users(id,username,password,enabled,account_non_expired,credentials_non_expired,account_non_locked)" +
            " values(#{id}, #{username},#{password},#{enabled},#{accountNonExpired},#{credentialsNonExpired},#{accountNonLocked})")
    Integer insert(User user);

}