package com.makiyo.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.makiyo.form.TestSayHelloForm;
import com.makiyo.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/test")
@Api("测试")
public class TestController {
    @Value("${eps.face.checkinUrl}")
    private String checkinUrl;

    @PostMapping("/sayHello")
    @ApiOperation("测试方法")
    public Response sayHello(@Valid @RequestBody TestSayHelloForm form)
    {
        return Response.ok().put("message","hello world,"+form.getName());
    }

    public static void main(String[] args) {
        int userId = 171820;
        String secret = "lin171820";
        Date date = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR,5);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create();
        String token = builder.withClaim("userId",userId).withExpiresAt(date).sign(algorithm);
        System.out.println(token);
    }

    @PostMapping("/addUser")
    @ApiOperation("添加用户")
    @RequiresPermissions(value = {"ROOT","USER:ADD"},logical = Logical.OR)
    public Response addUser()
    {
        return Response.ok("用户添加成功");
    }

    @PostMapping("/testFace")
    @ApiOperation("检测人脸")
    public void testFace(int userId, String path)
    {
        HttpRequest request = HttpUtil.createPost("http://192.168.31.152:5001/createFaceModel");
        request.form("photo", FileUtil.file(path));
        HttpResponse response = request.execute();
        String body = response.body();
        System.out.println(body);
    }
}
