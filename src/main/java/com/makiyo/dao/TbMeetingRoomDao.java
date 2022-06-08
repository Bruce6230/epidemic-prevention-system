package com.makiyo.dao;

import com.makiyo.pojo.TbMeetingRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbMeetingRoomDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TbMeetingRoom record);

    int insertSelective(TbMeetingRoom record);

    TbMeetingRoom selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbMeetingRoom record);

    int updateByPrimaryKey(TbMeetingRoom record);
}
