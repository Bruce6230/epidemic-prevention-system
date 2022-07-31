package com.makiyo.dao;

import com.makiyo.pojo.TbMeeting;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public boolean searchMeetingMembersInSameDept(String uuid);

    public int updateMeetingInstanceId(HashMap map);

    public HashMap searchMeetingById(int id);

    public ArrayList<HashMap> searchMeetingMembers(int id);

    public int updateMeetingInfo(HashMap param);

    public int deleteMeetingById(int id);

    public List<String> searchUserMeetingInMonth(HashMap param);
}
