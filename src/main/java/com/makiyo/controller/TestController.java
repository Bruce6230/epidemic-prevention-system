package com.makiyo.controller;

import com.makiyo.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api("测试")
public class TestController {

    @GetMapping("/sayHello")
    @ApiOperation("测试方法")
    public Response sayHello()
    {
        return Response.ok().put("message","hello world");
    }
}
