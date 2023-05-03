package com.renzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.SaveUserDto;
import com.renzf.domain.dto.UpdateUserDto;
import com.renzf.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-04-23 11:10:13
 */

public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult saveUser(SaveUserDto saveUserDto);

    ResponseResult deleteRole(Long id);



    ResponseResult diaplayUser(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);
}

