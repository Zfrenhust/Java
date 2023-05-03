package com.renzf.service.impl;

import com.renzf.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    //判断当前用户是否具有权限
    public boolean hasPermission(String permission){
        //如果是超级管理员直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
        //否则获取当前登录用户所具有的权限列表 判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
