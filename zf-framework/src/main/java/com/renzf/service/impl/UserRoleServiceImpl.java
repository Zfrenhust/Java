package com.renzf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.domain.entity.UserRole;
import com.renzf.mapper.UserRoleMapper;
import com.renzf.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-04-28 20:45:00
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

