package com.makiyo.aop;

import com.makiyo.config.ThreadLocalToken;
import com.makiyo.utils.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenAspect {
    @Autowired
    private ThreadLocalToken threadLocalToken;

    @Pointcut("execution(public * com.makiyo.controller.*.*(..)))")
    public void aspect()
    {

    }
    @Around("aspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Response response = (Response) point.proceed();
        String token = threadLocalToken.getToken();
        if(token!=null)
        {
            response.put("token",token);
            threadLocalToken.clear();
        }
        return response;

    }
}
