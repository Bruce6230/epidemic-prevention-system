package com.makiyo.controller;

import cn.hutool.json.JSONUtil;
import com.makiyo.exception.EpsException;
import com.makiyo.form.*;
import com.makiyo.service.UserService;
import com.makiyo.utils.JwtUtil;
import com.makiyo.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @PostMapping("/searchUserGroupByDept")
    @ApiOperation("查询员工列表，按照部门分组排列")
    @RequiresPermissions(value = {"ROOT","EMPLOYEE:SELECT"},logical = Logical.OR)
    public Response searchUserGroupByDept(@Valid @RequestBody SearchUserGroupByDeptForm form){
        ArrayList<HashMap> list=userService.searchUserGroupByDept(form.getKeyword());
        return Response.ok().put("result",list);
    }

    @PostMapping("/searchMembers")
    @ApiOperation("查询成员")
    @RequiresPermissions(value = {"ROOT", "MEETING:INSERT", "MEETING:UPDATE"},logical = Logical.OR)
    public Response searchMembers(@Valid @RequestBody SearchMembersForm form){
        if(!JSONUtil.isJsonArray(form.getMembers())){
            throw new EpsException("members不是JSON数组");
        }
        List param=JSONUtil.parseArray(form.getMembers()).toList(Integer.class);
        ArrayList list=userService.searchMembers(param);
        return Response.ok().put("result",list);
    }

//    @PostMapping("/webLogin")
//    @ApiOperation("web端登录系统")
//    public Response webLogin(@Valid @RequestBody WebLoginForm form)
//    {
//        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
//        Integer userId = userService.webLogin(param);
//        Response response = Response.ok().put("result",userId != null ? true : false);
//        if(userId != null){
//            String token = jwtUtil.createToken(userId);
//            saveCacheToken(token,userId);
//            Set<String> permissions = userService.searchUserPermissions(userId);
//            response.put("permissions",permissions);
//        }
//        return response;
//    }

    @PostMapping("/selectUserPhotoAndName")
    @ApiOperation("查询用户姓名和头像")
    @RequiresPermissions(value = {"WORKFLOW:APPROVAL"})
    public Response selectUserPhotoAndName(@Valid @RequestBody SelectUserPhotoAndNameForm form){
        if(!JSONUtil.isJsonArray(form.getIds())){
            throw new EpsException("参数不是JSON数组");
        }
        List<Integer> param=JSONUtil.parseArray(form.getIds()).toList(Integer.class);
        List<HashMap> list=userService.selectUserPhotoAndName(param);
        return Response.ok().put("result",list);
    }

}
