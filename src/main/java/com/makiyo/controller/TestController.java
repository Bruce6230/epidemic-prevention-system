package com.makiyo.controller;

import com.makiyo.form.TestSayHelloForm;
import com.makiyo.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
@Api("测试")
public class TestController {

    @PostMapping("/sayHello")
    @ApiOperation("测试方法")
    public Response sayHello(@Valid @RequestBody TestSayHelloForm form)
    {
        return Response.ok().put("message","hello world,"+form.getName());
    }
}
