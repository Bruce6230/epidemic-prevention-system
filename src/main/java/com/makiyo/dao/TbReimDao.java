package com.makiyo.dao;

import com.makiyo.pojo.TbReim;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbReimDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TbReim record);

    int insertSelective(TbReim record);

    TbReim selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbReim record);

    int updateByPrimaryKey(TbReim record);
}
