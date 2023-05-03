package com.renzf.controller;

import com.renzf.domain.ResponseResult;
import com.renzf.domain.entity.LoginUser;
import com.renzf.domain.entity.Menu;
import com.renzf.domain.entity.User;
import com.renzf.domain.vo.AdminUserInfoVo;
import com.renzf.domain.vo.RoutersVo;
import com.renzf.domain.vo.UserInfoVo;
import com.renzf.enums.AppHttpCodeEnum;
import com.renzf.exception.SystemException;
import com.renzf.service.MenuService;
import com.renzf.service.RoleService;
import com.renzf.service.SystemLoginService;
import com.renzf.utils.BeanCopyUtils;
import com.renzf.utils.RedisCache;
import com.renzf.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SystemLoginController {
    @Autowired
    private SystemLoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;
    //登录
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示:必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);

        }
        return loginService.login(user);
    }

    //获得当前用户对应的信息 例如:权限 角色
    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPeremsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息UserInfoVo
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    //路由
    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @Autowired
    private RedisCache redisCache;
    //退出
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
