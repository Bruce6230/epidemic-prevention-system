package com.makiyo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.makiyo.config.SystemConstants;
import com.makiyo.exception.EpsException;
import com.makiyo.form.CheckinForm;
import com.makiyo.service.CheckinService;
import com.makiyo.service.UserService;
import com.makiyo.utils.JwtUtil;
import com.makiyo.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author makiyo
 * @create 2022-06-23 19:21
 */
@RequestMapping("/checkin")
@RestController
@Api("签到模块Web接口")
@Slf4j
public class CheckinController {
    @Value("${eps.image-folder}")
    private String imageFolder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CheckinService checkinService;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemConstants constants;

    @GetMapping("/validCanCheckIn")
    @ApiOperation("查看用户今天是否可以签到")
    public Response validCanCheckIn(@RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        String result=checkinService.validCanCheckIn(userId, DateUtil.today());
        return Response.ok(result);
    }

    /**
     * @param form
     * @param file 对应参数为photo
     * @return
     */
    @PostMapping("/checkin")
    @ApiOperation("签到")
    public Response checkin(@Valid CheckinForm form, @RequestParam("photo") MultipartFile file,@RequestHeader("token") String token)
    {
        if(file==null)
        {
//            如果客户端未上传照片
            return Response.error("未上传文件");
        }
        int userId = jwtUtil.getUserId(token);
        String fileName = file.getOriginalFilename().toLowerCase();
        if(!fileName.endsWith(".jpg")){
            return Response.error("必须提交JPG格式文件");
        }else{
            String path = imageFolder+"/"+fileName;
            try {
                file.transferTo(Paths.get(path));
                HashMap param = new HashMap();
                param.put("userId",userId);
                param.put("path",path);
                param.put("city",form.getCity());
                param.put("district",form.getDistrict());
                param.put("address",form.getAddress());
                param.put("province",form.getProvince());
                param.put("country",form.getCountry());
                checkinService.checkin(param);
                return Response.ok("签到成功");

            } catch (IOException e) {
                log.error(e.getMessage(),e);
                throw new EpsException("图片保存错误");
            }finally {
                FileUtil.del(path);
            }
        }

    }

    @PostMapping("/createFaceModel")
    @ApiOperation("创建人脸模型")
    public Response createFaceModel(@RequestParam("photo") MultipartFile file,@RequestHeader("token") String token){
        if(file==null)
        {
//            如果客户端未上传照片
            return Response.error("未上传文件");
        }
        int userId = jwtUtil.getUserId(token);
        String fileName = file.getOriginalFilename().toLowerCase();
        if(!fileName.endsWith(".jpg")){
            return Response.error("必须提交JPG格式文件");
        }else{
            String path = imageFolder+"/"+fileName;
            try {
                file.transferTo(Paths.get(path));
                checkinService.createFaceModel(userId,path);
                return Response.ok("人脸建模成功");

            } catch (IOException e) {
                log.error(e.getMessage(),e);
                throw new EpsException("图片保存错误");
            }finally {
                FileUtil.del(path);
            }
        }
    }

    @GetMapping("/searchTodayCheckin")
    @ApiOperation("查询用户当日签到数据")
    public Response searchTodayCheckin(@RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        HashMap map=checkinService.searchTodayCheckin(userId);
        map.put("attendanceTime",constants.attendanceTime);
        map.put("closingTime",constants.closingTime);
        long days=checkinService.searchCheckinDays(userId);
        map.put("checkinDays",days);

        DateTime hiredate=DateUtil.parse(userService.searchUserHiredate(userId));
        DateTime startDate=DateUtil.beginOfWeek(DateUtil.date());
        if(startDate.isBefore(hiredate)){
            startDate=hiredate;
        }
        DateTime endDate=DateUtil.endOfWeek(DateUtil.date());
        HashMap param=new HashMap();
        param.put("startDate",startDate.toString());
        param.put("endDate",endDate.toString());
        param.put("userId",userId);
        ArrayList<HashMap> list=checkinService.searchWeekCheckin(param);
        map.put("weekCheckin",list);
        return Response.ok().put("result",map);
    }
}
