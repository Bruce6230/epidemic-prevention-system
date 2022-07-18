package com.makiyo.controller;

import cn.hutool.core.util.StrUtil;
import com.makiyo.form.LoginForm;
import com.makiyo.form.RegisterForm;
import com.makiyo.service.UserService;
import com.makiyo.utils.JwtUtil;
import com.makiyo.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author makiyo
 * @create 2022-06-21 16:52
 */
@RestController
@RequestMapping("/user")
@Api("用户模块Web接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${eps.jwt.cache-expire}")
    private int cacheExpire;

    @PostMapping("/register")
    @ApiOperation("注册用户")
    public Response register(@Valid @RequestBody RegisterForm form){
        int id=userService.registerUser(form.getRegisterCode(),form.getCode(),form.getNickname(),form.getPhoto());
        String token=jwtUtil.createToken(id);
        Set<String> permsSet=userService.searchUserPermissions(id);
        saveCacheToken(token,id);
        return Response.ok("用户注册成功").put("token",token).put("permission",permsSet);
    }

    @PostMapping("/login")
    @ApiOperation("登录系统")
    public Response login(@Valid @RequestBody LoginForm form)
    {
        int id = userService.login(form.getCode());
        String token = jwtUtil.createToken(id);
        saveCacheToken(token,id);
        Set<String> permSet = userService.searchUserPermissions(id);
        return Response.ok("登录成功").put("token",token).put("permission",permSet);
    }

    @GetMapping("/searchUserSummary")
    @ApiOperation("查询用户信息")
    public Response searchUserSummary(@RequestHeader("token") String token){
        int userId = jwtUtil.getUserId(token);
        HashMap map = userService.searchUserSummary(userId);
        return Response.ok().put("result",map);
    }

    private void saveCacheToken(String token,int userId) {
        redisTemplate.opsForValue().set(token, userId + "", cacheExpire, TimeUnit.DAYS);
    }
}
