package com.renzf.controller;


import com.renzf.annotation.SystemLog;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.entity.User;
import com.renzf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    //获取用户的个人信息
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    //更新用户个人信息
    @PutMapping("/userInfo")
    @SystemLog(bussinessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    //注册新用户
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
