package com.lzj.admin.utils;


import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.model.RespBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice //对于所有的controller进行增强
public class GlobalExceptionHandler {

    @ExceptionHandler(ParamsException.class)
    @ResponseBody //返回一个json
    public RespBean paramsExceptionHandler(ParamsException e){
        return RespBean.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RespBean exceptionHandler(Exception e){
        return RespBean.error(e.getMessage());
    }

}
