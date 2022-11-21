package com.makiyo.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.makiyo.dao.TbMeetingDao;
import com.makiyo.dao.TbUserDao;
import com.makiyo.exception.EpsException;
import com.makiyo.pojo.TbMeeting;
import com.makiyo.service.MeetingService;
import com.makiyo.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Makiyo
 * @date 2022/7/21 22:31
 */
@Service
@Slf4j
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private TbMeetingDao tbMeetingDao;

    @Autowired
    private TbUserDao userDao;

    @Value("${workflow.url}")
    private String workflow;

    @Value("${eps.recieveNotify}")
    private String recieveNotify;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void insertMeeting(TbMeeting tbMeeting) {
        int row = tbMeetingDao.insertMeeting(tbMeeting);
        if (row != 1) {
            throw new EpsException("会议添加失败");
        }
        //开启审批工作流
        startMeetingWorkflow(tbMeeting.getUuid(), tbMeeting.getCreatorId().intValue(), tbMeeting.getDate(), tbMeeting.getStart());
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
    private void startMeetingWorkflow(String uuid, int creatorId, String date, String start) {
        HashMap info = userDao.searchUserInfo(creatorId);
        JSONObject json = new JSONObject();
        json.set("url", recieveNotify);
        json.set("uuid", uuid);
        json.set("openId", info.get("openId"));
        json.set("date", date);
        json.set("start", start);
        String[] roles = info.get("roles").toString().split("，");
        if (!ArrayUtil.contains(roles, "总经理")) {
            Integer managerId = userDao.searchDeptManagerId(creatorId);
            json.set("managerId", managerId);
            Integer gmId = userDao.searchGmId();
            json.set("gmId", gmId);
            boolean bool = tbMeetingDao.searchMeetingMembersInSameDept(uuid);
            json.set("sameDept", bool);
        }
        String url = workflow + "/workflow/startMeetingProcess";
        HttpResponse resp = HttpRequest.post(url).header("Content-Type", "application/json")
                .body(json.toString()).execute();
        if (resp.getStatus() == 200) {
            json = JSONUtil.parseObj(resp.body());
            String instanceId = json.getStr("instanceId");
            HashMap param = new HashMap();
            param.put("uuid", uuid);
            param.put("instanceId", instanceId);
            int row = tbMeetingDao.updateMeetingInstanceId(param);
            if (row != 1) {
                log.error("保存会议工作流实例ID失败");
                throw new EpsException("保存会议工作流实例ID失败");
            }
        }
    }

    @Override
    public void updateMeetingInfo(HashMap param) {
        int id = (int) param.get("id");
        String date = param.get("date").toString();
        String start = param.get("start").toString();
        String instanceId = param.get("instanceId").toString();
        HashMap oldMeeting = tbMeetingDao.searchMeetingById(id);
        String uuid = oldMeeting.get("uuid").toString();
        Integer creatorId = Integer.parseInt(oldMeeting.get("creatorId").toString());
        int row = tbMeetingDao.updateMeetingInfo(param);
        if (row != 1) {
            throw new EpsException("会议更新失败");
        }
        JSONObject json = new JSONObject();
        json.set("instanceId", instanceId);
        json.set("reason", "会议被修改");
        json.set("uuid", uuid);
        String url = workflow + "/workflow/deleteProcessById";
        HttpResponse resp = HttpRequest.post(url).header("content-type", "application/json")
                .body(json.toString()).execute();
        if (resp.getStatus() != 200) {
            log.error("删除工作流失败");
            throw new EpsException("删除工作流失败");
        }
        startMeetingWorkflow(uuid, creatorId, date, start);
    }

    @Override
    public void deleteMeetingById(int id) {
        HashMap meeting = tbMeetingDao.searchMeetingById(id);
        String uuid = meeting.get("uuid").toString();
        String instanceId = meeting.get("instanceId").toString();
        DateTime date = DateUtil.parse(meeting.get("date") + " " + meeting.get("start"));
        DateTime now = DateUtil.date();
        if (now.isAfterOrEquals(date.offset(DateField.MINUTE, -20))) {
            throw new EpsException("距离会议开始不足20分钟，不能删除会议");
        }
        int row = tbMeetingDao.deleteMeetingById(id);
        if (row != 1) {
            log.error("删除工作流失败");
            throw new EpsException("会议删除失败");
        }
        JSONObject json = new JSONObject();
        json.set("instanceId", instanceId);
        json.set("reason", "会议被修改");
        json.set("uuid", uuid);
        String url = workflow + "/workflow/deleteProcessById";
        HttpResponse resp = HttpRequest.post(url).header("content-type", "application/json")
                .body(json.toString()).execute();
        if (resp.getStatus() != 200) {
            log.error("删除工作流失败");
            throw new EpsException("删除工作流失败");
        }
    }

    @Override
    public HashMap searchMeetingById(int id) {
        HashMap map = tbMeetingDao.searchMeetingById(id);
        ArrayList<HashMap> list = tbMeetingDao.searchMeetingMembers(id);
        map.put("members",list);
        return map;
    }

    @Override
    public Long searchRoomIdByUUID(String uuid) {
        Object temp=redisTemplate.opsForValue().get(uuid);
        long roomId=Long.parseLong(temp.toString());
        return roomId;
    }

    @Override
    public List<String> searchUserMeetingInMonth(HashMap param) {
        List<String> list=tbMeetingDao.searchUserMeetingInMonth(param);
        return list;
    }

}
