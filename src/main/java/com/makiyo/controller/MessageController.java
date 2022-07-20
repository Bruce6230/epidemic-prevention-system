package com.makiyo.controller;

import com.makiyo.form.DeleteMessageRefByIdForm;
import com.makiyo.form.SearchMessageByIdForm;
import com.makiyo.form.SearchMessageByPageForm;
import com.makiyo.form.UpdateUnreadMessageForm;
import com.makiyo.service.MessageService;
import com.makiyo.utils.JwtUtil;
import com.makiyo.utils.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * @author Makiyo
 * @date 2022/7/21 2:28
 */
@RestController
@RequestMapping("/message")
@ApiOperation("消息模块接口")
public class MessageController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MessageService messageService;

//    @Autowired
//    private MessageTask messageTask;

    @PostMapping("/searchMessageByPage")
    @ApiOperation("获取分页消息列表")
    public Response searchMessageByPage(@Valid @RequestBody SearchMessageByPageForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        int page = form.getPage();
        int length = form.getLength();
        long start = (page - 1) * length;
        List<HashMap> list = messageService.searchMessageByPage(userId, start, length);
        return Response.ok().put("result", list);
    }

    @PostMapping("/searchMessageById")
    @ApiOperation("根据ID查询消息")
    public Response searchMessageById(@Valid @RequestBody SearchMessageByIdForm form) {
        HashMap map = messageService.searchMessageById(form.getId());
        return Response.ok().put("result", map);
    }

    @PostMapping("/updateUnreadMessage")
    @ApiOperation("未读消息更新成已读消息")
    public Response updateUnreadMessage(@Valid @RequestBody UpdateUnreadMessageForm form) {
        long rows = messageService.updateUnreadMessage(form.getId());
        return Response.ok().put("result", rows == 1 ? true : false);
    }

    @PostMapping("/deleteMessageRefById")
    @ApiOperation("删除消息")
    public Response deleteMessageRefById(@Valid @RequestBody DeleteMessageRefByIdForm form) {
        long rows = messageService.deleteMessageRefById(form.getId());
        return Response.ok().put("result", rows == 1 ? true : false);
    }

//    @GetMapping("/refreshMessage")
//    @ApiOperation("刷新用户消息")
//    public Response refreshMessage(@RequestHeader("token") String token){
//        int userId=jwtUtil.getUserId(token);
//        messageTask.receiveAsync(userId+"");
//        long lastRows=messageService.searchLastCount(userId);
//        long unreadRows=messageService.searchUnreadCount(userId);
//        return Response.ok().put("lastRows",lastRows).put("unreadRows",unreadRows);
//    }
}
