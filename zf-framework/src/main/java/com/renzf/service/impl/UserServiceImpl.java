package com.renzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.SaveUserDto;
import com.renzf.domain.dto.UpdateUserDto;
import com.renzf.domain.entity.Role;
import com.renzf.domain.entity.User;
import com.renzf.domain.entity.UserRole;
import com.renzf.domain.vo.*;
import com.renzf.enums.AppHttpCodeEnum;
import com.renzf.exception.SystemException;
import com.renzf.mapper.RoleMapper;
import com.renzf.mapper.UserMapper;
import com.renzf.service.RoleService;
import com.renzf.service.UserRoleService;
import com.renzf.service.UserService;
import com.renzf.utils.BeanCopyUtils;
import com.renzf.utils.SecurityUtils;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-23 11:20:08
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //获取用户信息
    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    //更新用户信息
    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    //注册用户信息
    @Override
    public ResponseResult register(User user) {
        //对数据非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICK_NOT_NULL);
        }
        //不为空 对数据进行重复判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(userNickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICK_EXIST);
        }
        if(userEmailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存数据库
        save(user);
        return ResponseResult.okResult();
    }

    //查询用户列表
    @Override
    public ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.like(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.like(StringUtils.hasText(status),User::getStatus,status);
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class);
        return ResponseResult.okResult(new PageVo(userListVos,page.getTotal()));
    }

    @Autowired
    private UserRoleService userRoleService;
    //新增用户第二步:将信息保存到数据库
    @Override
    @Transactional
    public ResponseResult saveUser(SaveUserDto saveUserDto) {
        //对数据非空判断
        if(!StringUtils.hasText(saveUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(saveUserDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(saveUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICK_NOT_NULL);
        }
        //不为空 对数据进行重复判断
        if(userNameExist(saveUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(userNickNameExist(saveUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICK_EXIST);
        }
        if(userEmailExist(saveUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        User user = BeanCopyUtils.copyBean(saveUserDto, User.class);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        save(user);
        List<String> roleIds = saveUserDto.getRoleIds();
        for (String roleId : roleIds) {
            userRoleService.save(new UserRole(user.getId(),Long.parseLong(roleId)));
        }
        return ResponseResult.okResult();
    }

    //删除用户
    @Override
    public ResponseResult deleteRole(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Autowired
    private UserService userService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleService roleService;
    //更新用户第一步:回显
    @Override
    public ResponseResult diaplayUser(Long id) {
        User user = userService.getById(id);
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
        UpdateUserVo updateUserVo = new UpdateUserVo();
        updateUserVo.setUser(userVo);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> list = userRoleService.list(queryWrapper);
        List<String> roleIds = new ArrayList<>();
        for (UserRole userRole : list) {
            roleIds.add(userRole.getRoleId().toString());
        }
        updateUserVo.setRoleIds(roleIds);

        List<Role> roleList = roleService.list(null);
        List<UpdateUserRoleVo> updateUserRoleVos = BeanCopyUtils.copyBeanList(roleList, UpdateUserRoleVo.class);
        updateUserVo.setRoles(updateUserRoleVos);
        System.out.println(updateUserRoleVos);
        return ResponseResult.okResult(updateUserVo);
    }

    @Override
    @Transactional
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        updateById(user);
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(queryWrapper);
        List<String> roleIds = updateUserDto.getRoleIds();
        for (String roleId : roleIds) {
            userRoleService.save(new UserRole(user.getId(),Long.parseLong(roleId)));
        }
        return ResponseResult.okResult();
    }


    private boolean userEmailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }

    private boolean userNickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }
}

