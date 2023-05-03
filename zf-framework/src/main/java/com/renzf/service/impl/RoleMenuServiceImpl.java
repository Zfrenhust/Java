package com.renzf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.domain.entity.RoleMenu;
import com.renzf.mapper.RoleMenuMapper;
import com.renzf.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-28 12:16:07
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

