package com.makiyo.dao;

import com.makiyo.pojo.TbAmectType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbAmectTypeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TbAmectType record);

    int insertSelective(TbAmectType record);

    TbAmectType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbAmectType record);

    int updateByPrimaryKey(TbAmectType record);
}
