package com.renzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AddRoleDto;
import com.renzf.domain.dto.ChangeRoleDto;
import com.renzf.domain.dto.UpdateRoleDto;
import com.renzf.domain.entity.Menu;
import com.renzf.domain.entity.Role;
import com.renzf.domain.entity.RoleMenu;
import com.renzf.domain.vo.*;
import com.renzf.mapper.RoleMapper;
import com.renzf.mapper.RoleMenuMapper;
import com.renzf.service.MenuService;
import com.renzf.service.RoleMenuService;
import com.renzf.service.RoleService;
import com.renzf.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-04-25 16:42:45
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult<PageVo> queryRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getDelFlag,0);
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,0);
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);
        List<Role> roleList = BeanCopyUtils.copyBeanList(page.getRecords(), Role.class);
        PageVo pageVo = new PageVo(roleList,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeRoleStatus(ChangeRoleDto changeRoleDto) {
        Role role = getById(changeRoleDto.getRoleId());
        role.setStatus(changeRoleDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        //把新用户信息保存在sys_role中
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        for (String menuId : addRoleDto.getMenuIds()) {
            RoleMenu roleMenu = new RoleMenu(role.getId(), Long.parseLong(menuId));
            roleMenuService.save(roleMenu);
        }
        return ResponseResult.okResult();
    }

    //修改角色:第一步数据回显
    @Override
    public ResponseResult roleDataDisplay(Long id) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId,id);
        Role role = getBaseMapper().selectOne(queryWrapper);
        AdminUpdateRoleVo adminUpdateRoleVo = BeanCopyUtils.copyBean(role, AdminUpdateRoleVo.class);
        return ResponseResult.okResult(adminUpdateRoleVo);
    }
    //修改角色:第二步获得菜单tree
    @Autowired
    private MenuService menuService;
    @Override
    public ResponseResult roleMenuTreeById(Long id) {
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(id);
        List<RoleMenuVo> roleMenuVos = BeanCopyUtils.copyBeanList(menus, RoleMenuVo.class);
        List<RoleMenuVo> tree = builderRoleMenuTree(roleMenuVos,0);
        List<String> checkedKeys = new ArrayList<>();
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> roleMenus = roleMenuService.list(queryWrapper);
        for (RoleMenu roleMenu : roleMenus) {
            checkedKeys.add(roleMenu.getMenuId().toString());
        }
        return ResponseResult.okResult(new UpdateRoleRoutersVo(tree,checkedKeys));
    }
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    //更新角色信息
    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);
        List<String> menuIds = updateRoleDto.getMenuIds();
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuService.remove(queryWrapper);
        for (String menuId : menuIds) {
            roleMenuService.save(new RoleMenu(role.getId(),Long.parseLong(menuId)));
        }
        return ResponseResult.okResult();
    }

    //删除用户
    @Override
    public ResponseResult deleteRole(Long id) {
        removeById(id);
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuMapper.delete(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> roles = getBaseMapper().selectList(null);
        List<UserRoleVo> userRoleVos = BeanCopyUtils.copyBeanList(roles, UserRoleVo.class);
        return ResponseResult.okResult(userRoleVos);
    }


    //构建Tree2方法
    private List<RoleMenuVo> builderRoleMenuTree(List<RoleMenuVo> roleMenuVos, long parentId){
        List<RoleMenuVo> roleMenuTree = roleMenuVos.stream()
                .filter(roleMenuVo -> roleMenuVo.getParentId().equals(parentId))
                .map(roleMenuVo -> roleMenuVo.setChildren(getRoleChildren(roleMenuVo, roleMenuVos)))
                .collect(Collectors.toList());
        return roleMenuTree;
    }
    //获取roleMen中传入参数的子roleMenu
    private List<RoleMenuVo> getRoleChildren(RoleMenuVo roleMenuVo,List<RoleMenuVo> roleMenuVos){
        List<RoleMenuVo> list = roleMenuVos.stream()
                .filter(r -> r.getParentId().equals(roleMenuVo.getId()))
                .map(r -> r.setChildren(getRoleChildren(r, roleMenuVos)))
                .collect(Collectors.toList());
        return list;
    }
}

