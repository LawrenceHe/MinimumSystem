package com.example.usercenter.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserAssetMapper {

    @Select("SELECT fortune FROM assets WHERE id = #{id}")
    String getFortuneById(Long id);

    @Select("SELECT loan FROM assets WHERE id = #{id}")
    String getLoanById(Long id);

}
