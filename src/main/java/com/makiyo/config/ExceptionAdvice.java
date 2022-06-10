package com.makiyo.config;


import com.makiyo.exception.EpsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e)
    {
        log.error("执行异常",e);
        if(e instanceof MethodArgumentNotValidException)
        {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            return exception.getBindingResult().getFieldError().getDefaultMessage();
        }else if(e instanceof EpsException){
            EpsException exception = (EpsException) e;
            return exception.getMsg();
        }else if(e instanceof UnauthenticatedException){
            return "你不具备相关权限";
        }else{
            return "后端执行异常";
        }
    }
}
