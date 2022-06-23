package com.makiyo.dao;

import com.makiyo.pojo.TbWorkday;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbWorkdayDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TbWorkday record);

    int insertSelective(TbWorkday record);

    TbWorkday selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbWorkday record);

    int updateByPrimaryKey(TbWorkday record);

    public Integer searchTodayIsWorkday();
}
