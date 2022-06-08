package com.makiyo.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(!StrUtil.hasEmpty(value))
        {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values!=null)
        {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if(!StrUtil.hasEmpty(value))
                {
                    value = HtmlUtil.filter(value);
                }
                values[i]=value;
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String,String[]> parameters = super.getParameterMap();
        LinkedHashMap<String,String[]> map = new LinkedHashMap();
        if(parameters!=null)
        {
            for (String key:parameters.keySet()
                 ) {
                String[] values = parameters.get(key);
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if(!StrUtil.hasEmpty(value))
                    {
                        value = HtmlUtil.filter(value);
                    }
                    values[i]=value;
                }
                map.put(key,values);
            }
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value =  super.getHeader(name);
        if(!StrUtil.hasEmpty(value))
        {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        InputStream inputStream =  super.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        StringBuffer body = new StringBuffer();
        String line = bufferedReader.readLine();
        while (line!=null)
        {
            body.append(line);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        streamReader.close();
        inputStream.close();
        Map<String,Object> map = JSONUtil.parseObj(body.toString());
        Map<String,Object> result = new LinkedHashMap<>();
        for (String key :
                map.keySet()) {
            Object value = map.get(key);
            if(value instanceof String)
            {
                if(!StrUtil.hasEmpty(value.toString()))
                {
                    result.put(key,HtmlUtil.filter(value.toString()));
                }
            }else{
                result.put(key,value);
            }
        }
        String json = JSONUtil.toJsonStr(result);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }
}
