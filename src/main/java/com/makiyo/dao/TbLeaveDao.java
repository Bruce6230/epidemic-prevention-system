package com.makiyo.dao;

import com.makiyo.pojo.TbLeave;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbLeaveDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TbLeave record);

    int insertSelective(TbLeave record);

    TbLeave selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbLeave record);

    int updateByPrimaryKey(TbLeave record);
}
