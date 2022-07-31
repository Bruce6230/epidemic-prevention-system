package com.makiyo.service;

import com.makiyo.pojo.TbMeeting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Makiyo
 * @date 2022/7/21 22:30
 */
public interface MeetingService {
    public void insertMeeting(TbMeeting tbMeeting);

    public ArrayList<HashMap> searchMyMeetingListByPage(HashMap param);

    public void updateMeetingInfo(HashMap param);

    public void deleteMeetingById(int id);
}
