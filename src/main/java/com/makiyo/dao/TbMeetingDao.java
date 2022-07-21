package com.makiyo.dao;

import com.makiyo.pojo.TbMeeting;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbMeetingDao {
    int deleteByPrimaryKey(Long id);

    int insert(TbMeeting record);

    int insertSelective(TbMeeting record);

    TbMeeting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbMeeting record);

    int updateByPrimaryKey(TbMeeting record);

    public int insertMeeting(TbMeeting tbMeeting);

    public ArrayList<HashMap> searchMyMeetingListByPage(HashMap param);
}
