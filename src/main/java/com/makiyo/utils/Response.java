package com.makiyo.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class Response extends HashMap<String,Object> {

    public Response() {
        put("code", HttpStatus.SC_OK);
        put("msg","success");
    }

    public Response put(String key,Object value)
    {
        super.put(key,value);
        return this;
    }

    public static Response ok()
    {
        return new Response();
    }

    public static Response ok(String msg)
    {
        Response response = new Response();
        response.put("msg",msg);
        return response;
    }

    public static Response ok(Map<String,Object> map)
    {
        Response response = new Response();
        response.putAll(map);
        return response;
    }

    public static Response error(int code,String msg)
    {
        Response response = new Response();
        response.put("code",code);
        response.put("msg",msg);
        return response;
    }

    public static Response error(String msg)
    {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,msg);
    }

    public static Response error()
    {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"未知异常，请联系管理员");
    }
}
