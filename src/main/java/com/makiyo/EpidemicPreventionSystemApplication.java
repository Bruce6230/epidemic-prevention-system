package com.makiyo;

import cn.hutool.core.util.StrUtil;
import com.makiyo.config.SystemConstants;
import com.makiyo.dao.SysConfigDao;
import com.makiyo.pojo.SysConfig;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@SpringBootApplication
@ServletComponentScan
@Slf4j
@EnableAsync
public class EpidemicPreventionSystemApplication {

    @Autowired
    private SysConfigDao sysConfigDao;

    @Autowired
    private SystemConstants systemConstants;

    @PostConstruct
    public void init()
    {
        List<SysConfig> list = sysConfigDao.selectAllParam();
        list.forEach(one->{
            String key = one.getParamKey();
            key= StrUtil.toCamelCase(key);
            String value = one.getParamValue();
            try {
                Field field = systemConstants.getClass().getDeclaredField(key);
                field.set(systemConstants,value);
            } catch (Exception e) {
                log.error("执行异常",e);
            }
        });
    }
    public static void main(String[] args) {
        SpringApplication.run(EpidemicPreventionSystemApplication.class, args);
    }

}
