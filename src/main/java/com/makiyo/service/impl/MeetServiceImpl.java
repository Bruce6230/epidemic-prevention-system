package com.makiyo.service.impl;

import cn.hutool.json.JSONArray;
import com.makiyo.dao.TbMeetingDao;
import com.makiyo.exception.EpsException;
import com.makiyo.pojo.TbMeeting;
import com.makiyo.service.MeetingService;
import com.makiyo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Makiyo
 * @date 2022/7/21 22:31
 */
@Service
public class MeetServiceImpl implements MeetingService {
    @Autowired
    private TbMeetingDao tbMeetingDao;

    @Override
    public void insertMeeting(TbMeeting tbMeeting) {
        int row = tbMeetingDao.insertMeeting(tbMeeting);
        if (row != 1) {
            throw new EpsException("会议添加失败");
        }
        //开启审批工作流
//        EpsException(tbMeeting.getUuid(), tbMeeting.getCreatorId().intValue(), tbMeeting.getDate(), tbMeeting.getStart());
    }

    @Override
    public ArrayList<HashMap> searchMyMeetingListByPage(HashMap param) {
        ArrayList<HashMap> list = tbMeetingDao.searchMyMeetingListByPage(param);
        String date = null;
        ArrayList resultList = new ArrayList();
        HashMap resultMap = null;
        JSONArray array = null;
        for (HashMap map : list) {
            String temp = map.get("date").toString();
            if (!temp.equals(date)) {
                date = temp;
                resultMap = new HashMap();
                resultMap.put("date", date);
                array = new JSONArray();
                resultMap.put("list", array);
                resultList.add(resultMap);
            }
            array.put(map);
        }
        return resultList;
    }
}
