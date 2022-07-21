package com.makiyo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.makiyo.form.SearchMyMeetingListByPageForm;
import com.makiyo.pojo.TbMeeting;
import com.makiyo.service.MeetingService;
import com.makiyo.utils.JwtUtil;
import com.makiyo.utils.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Makiyo
 * @date 2022/7/21 22:50
 */
@RestController
@RequestMapping("/meeting")
@Slf4j
public class MeetingController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/searchMyMeetingListByPage")
    @ApiOperation("查询会议列表分页数据")
    public Response searchMyMeetingListByPage(@Valid @RequestBody SearchMyMeetingListByPageForm form, @RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        int page=form.getPage();
        int length=form.getLength();
        long start=(page-1)*length;
        HashMap map=new HashMap();
        map.put("userId",userId);
        map.put("start",start);
        map.put("length",length);
        ArrayList list=meetingService.searchMyMeetingListByPage(map);
        return Response.ok().put("result",list);
    }

//    @PostMapping("/insertMeeting")
//    @ApiOperation("添加会议")
//    @RequiresPermissions(value = {"ROOT", "MEETING:INSERT"},logical = Logical.OR)
//    public Response insertMeeting(@Valid @RequestBody InsertMeetingForm form,@RequestHeader("token") String token){
//        if(form.getType()==2&&(form.getPlace()==null||form.getPlace().length()==0)){
//            throw new EmosException("线下会议地点不能为空");
//        }
//        DateTime d1= DateUtil.parse(form.getDate()+" "+form.getStart()+":00");
//        DateTime d2= DateUtil.parse(form.getDate()+" "+form.getEnd()+":00");
//        if(d2.isBeforeOrEquals(d1)){
//            throw new EmosException("结束时间必须大于开始时间");
//        }
//        if(!JSONUtil.isJsonArray(form.getMembers())){
//            throw new EmosException("members不是JSON数组");
//        }
//        TbMeeting entity=new TbMeeting();
//        entity.setUuid(UUID.randomUUID().toString(true));
//        entity.setTitle(form.getTitle());
//        entity.setCreatorId((long)jwtUtil.getUserId(token));
//        entity.setDate(form.getDate());
//        entity.setPlace(form.getPlace());
//        entity.setStart(form.getStart() + ":00");
//        entity.setEnd(form.getEnd() + ":00");
//        entity.setType((short)form.getType());
//        entity.setMembers(form.getMembers());
//        entity.setDesc(form.getDesc());
//        entity.setStatus((short)1);
//        meetingService.insertMeeting(entity);
//        return Response.ok().put("result","success");
//    }
//    @PostMapping("/searchMeetingById")
//    @ApiOperation("根据ID查询会议")
//    @RequiresPermissions(value = {"ROOT", "MEETING:SELECT"}, logical = Logical.OR)
//    public Response searchMeetingById(@Valid @RequestBody SearchMeetingByIdFrom form){
//        HashMap map=meetingService.searchMeetingById(form.getId());
//        return Response.ok().put("result",map);
//    }
//
//    @PostMapping("/updateMeetingInfo")
//    @ApiOperation("更新会议")
//    @RequiresPermissions(value = {"ROOT", "MEETING:UPDATE"}, logical = Logical.OR)
//    public Response updateMeetingInfo(@Valid @RequestBody UpdateMeetingInfoForm form){
//        if(form.getType()==2&&(form.getPlace()==null||form.getPlace().length()==0)){
//            throw new EmosException("线下会议地点不能为空");
//        }
//        DateTime d1= DateUtil.parse(form.getDate()+" "+form.getStart()+":00");
//        DateTime d2= DateUtil.parse(form.getDate()+" "+form.getEnd()+":00");
//        if(d2.isBeforeOrEquals(d1)){
//            throw new EmosException("结束时间必须大于开始时间");
//        }
//        if(!JSONUtil.isJsonArray(form.getMembers())){
//            throw new EmosException("members不是JSON数组");
//        }
//        HashMap param=new HashMap();
//        param.put("title", form.getTitle());
//        param.put("date", form.getDate());
//        param.put("place", form.getPlace());
//        param.put("start", form.getStart() + ":00");
//        param.put("end", form.getEnd() + ":00");
//        param.put("type", form.getType());
//        param.put("members", form.getMembers());
//        param.put("desc", form.getDesc());
//        param.put("id", form.getId());
//        param.put("instanceId", form.getInstanceId());
//        param.put("status", 1);
//        meetingService.updateMeetingInfo(param);
//        return Response.ok().put("result","success");
//    }
//    @PostMapping("/deleteMeetingById")
//    @ApiOperation("根据ID删除会议")
//    @RequiresPermissions(value = {"ROOT", "MEETING:DELETE"}, logical = Logical.OR)
//    public Response deleteMeetingById(@Valid @RequestBody DeleteMeetingByIdForm form){
//        meetingService.deleteMeetingById(form.getId());
//        return Response.ok().put("result","success");
//    }
//
//    @PostMapping("/recieveNotify")
//    @ApiOperation("接收工作流通知")
//    public Response recieveNotify(@Valid @RequestBody RecieveNotifyForm form){
//        if(form.getResult().equals("同意")){
//            log.debug(form.getUuid()+"的会议审批通过");
//        }
//        else{
//            log.debug(form.getUuid()+"的会议审批不通过");
//        }
//        return Response.ok();
//    }
//
//    @PostMapping("/searchRoomIdByUUID")
//    @ApiOperation("查询会议房间RoomID")
//    public Response searchRoomIdByUUID(@Valid @RequestBody SearchRoomIdByUUIDForm form){
//        long roomId=meetingService.searchRoomIdByUUID(form.getUuid());
//        return Response.ok().put("result",roomId);
//    }
//
//    @PostMapping("/searchUserMeetingInMonth")
//    @ApiOperation("查询某月用户的会议日期列表")
//    public Response searchUserMeetingInMonth(@Valid @RequestBody SearchUserMeetingInMonthForm form,@RequestHeader("token") String token){
//        int userId=jwtUtil.getUserId(token);
//        HashMap param=new HashMap();
//        param.put("userId",userId);
//        param.put("express",form.getYear()+"/"+form.getMonth());
//        List<String> list=meetingService.searchUserMeetingInMonth(param);
//        return R.ok().put("result",list);
//    }
}
