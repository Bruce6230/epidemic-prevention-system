package com.makiyo.dao;

import com.makiyo.pojo.TbAmect;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbAmectDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TbAmect record);

    int insertSelective(TbAmect record);

    TbAmect selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbAmect record);

    int updateByPrimaryKey(TbAmect record);
}
