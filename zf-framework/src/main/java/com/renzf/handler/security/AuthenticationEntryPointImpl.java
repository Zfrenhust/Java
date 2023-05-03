package com.renzf.handler.security;

import com.alibaba.fastjson.JSON;
import com.renzf.domain.ResponseResult;
import com.renzf.enums.AppHttpCodeEnum;
import com.renzf.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        ResponseResult responseResult = null;
        //不同情况下的错误信息不同
        if(e instanceof BadCredentialsException){
            responseResult = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),e.getMessage());
        }else if(e instanceof BadCredentialsException){
            responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(),e.getMessage());
        }else{
            responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");
        }
        //响应给前端
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
    }
}
