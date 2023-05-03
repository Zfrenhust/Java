package com.renzf.controller;


import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.SaveUserDto;
import com.renzf.domain.dto.UpdateUserDto;
import com.renzf.service.RoleService;
import com.renzf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //显示用户列表
    @GetMapping("system/user/list")
    public ResponseResult userList(Integer pageNum, Integer pageSize, @RequestParam(required = false) String userName,@RequestParam(required = false) String phonenumber,@RequestParam(required = false) String status){
        return userService.userList(pageNum,pageSize,userName,phonenumber,status);
    }

    @Autowired
    private RoleService roleService;

    //新增用户:显示可选择的角色
    @GetMapping("/system/role/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
    //新增用户:将数据保存到数据库中
    @PostMapping("/system/user")
    public ResponseResult saveUser(@RequestBody SaveUserDto saveUserDto){
        return userService.saveUser(saveUserDto);
    }

    //删除用户
    @DeleteMapping("/system/user/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return userService.deleteRole(id);
    }

    //修改用户:第一步信息回显
    @GetMapping("/system/user/{id}")
    public ResponseResult displayUser(@PathVariable("id") Long id){
        return userService.diaplayUser(id);
    }
    //修改用户:第二部保存信息
    @PutMapping("/system/user")
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
