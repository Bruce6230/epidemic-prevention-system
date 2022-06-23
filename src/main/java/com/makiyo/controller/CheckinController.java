package com.makiyo.controller;

import cn.hutool.core.date.DateUtil;
import com.makiyo.service.CheckinService;
import com.makiyo.utils.JwtUtil;
import com.makiyo.utils.Response;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author makiyo
 * @create 2022-06-23 19:21
 */
@RequestMapping("/checkin")
@RestController
@Api("签到模块web接口")
@Slf4j
public class CheckinController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CheckinService checkinService;

    public Response validCanCheckin(@RequestHeader("token") String token){
        int userId = jwtUtil.getUserId(token);
        String result = checkinService.validCanCheckIn(userId, DateUtil.today());
        return Response.ok(result);
    }
}
