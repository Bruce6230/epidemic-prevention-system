package com.makiyo.dao;

import com.makiyo.pojo.TbCheckin;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TbCheckinDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(TbCheckin record);

    TbCheckin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbCheckin record);

    int updateByPrimaryKey(TbCheckin record);

    public Integer haveCheckin(HashMap param);

    public void insert(TbCheckin checkin);
}
