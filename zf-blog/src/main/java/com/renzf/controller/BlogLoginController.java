package com.renzf.controller;


import com.renzf.domain.ResponseResult;
import com.renzf.domain.entity.User;
import com.renzf.enums.AppHttpCodeEnum;
import com.renzf.exception.SystemException;
import com.renzf.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    //登录
    @PostMapping("/login")
    public ResponseResult login(@RequestBody  User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示:必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);

        }
        return blogLoginService.login(user);
    }
    //退出
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }

}
