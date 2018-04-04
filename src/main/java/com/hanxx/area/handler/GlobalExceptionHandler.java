package com.hanxx.area.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//统一的异常处理
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    private Map<String,Object>exceptionHandler(HttpServletRequest req,Exception e){
        Map<String,Object> modeMap =new HashMap<String,Object>();
        modeMap.put("success",false);           //捕获到异常
        modeMap.put("errMsg",e.getMessage());   //错误信息
        return modeMap;
    }
}
