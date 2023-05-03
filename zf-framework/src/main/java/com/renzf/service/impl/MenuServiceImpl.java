package com.renzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.constants.SystemConstants;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.MenuDto;
import com.renzf.domain.dto.UpdateMenuDto;
import com.renzf.domain.entity.Menu;
import com.renzf.domain.entity.RoleMenu;
import com.renzf.domain.vo.AdminUpdateRoleVo;
import com.renzf.domain.vo.RoleMenuVo;
import com.renzf.domain.vo.UpdateRoleRoutersVo;
import com.renzf.enums.AppHttpCodeEnum;
import com.renzf.mapper.MenuMapper;
import com.renzf.service.MenuService;
import com.renzf.service.RoleMenuService;
import com.renzf.utils.BeanCopyUtils;
import com.renzf.utils.SecurityUtils;
import io.swagger.annotations.Authorization;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-25 16:35:42
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPeremsByUserId(Long id) {
        //如果是管理员返回所有的权限
            if(id == 1L){//if中的判断语句可以改为:SecurityUtils.isAdmin()
                LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
                queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
                List<Menu> menus = list(queryWrapper);
                List<String> perms = menus.stream()
                        .map(Menu::getPerms)
                        .collect(Collectors.toList());
                return perms;
            }
        //否则返回其所具有的权限

        return getBaseMapper().selectPeremsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){//如果是返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else {//否则 返回当前用户的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找第一层的菜单 然后再找子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }




    @Override
    public ResponseResult queryMenuList(String status, String menuName) {
//        List<Menu> list = null;
//        if(!StringUtils.hasText(status) || !StringUtils.hasText(menuName)){
//            list = list(null);
//        }else{
//            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Menu::getStatus,status);
//            queryWrapper.like(Menu::getMenuName,menuName);
//            list = list(queryWrapper);
//        }
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        List<Menu>list = list(queryWrapper);
        List<MenuDto> menuDtos = BeanCopyUtils.copyBeanList(list, MenuDto.class);
        return ResponseResult.okResult(menuDtos);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //TODO 判断条件有问题,把自己设置成上级菜单仍然可以更新
        if(menu.getParentId() == menu.getId()){
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_UPDATE);
        }else{
            updateById(menu);
            return ResponseResult.okResult();
        }
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        removeById(menuId);
        return ResponseResult.okResult();
    }

    //在新增角色中获取菜单tree
    @Override
    public ResponseResult queryRoleTree() {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = menuMapper.selectAllRouterMenu();
        List<RoleMenuVo> roleMenuVos = BeanCopyUtils.copyBeanList(menus, RoleMenuVo.class);
        List<RoleMenuVo> tree = builderRoleMenuTree(roleMenuVos,0);
        return ResponseResult.okResult(tree);
    }

    @Override
    public ResponseResult queryMenuById(Long id) {
        Menu menu = getBaseMapper().selectById(id);
        UpdateMenuDto updateMenuDto = BeanCopyUtils.copyBean(menu, UpdateMenuDto.class);
        return ResponseResult.okResult(updateMenuDto);
    }


    //构建Tree方法
    private List<Menu> builderMenuTree(List<Menu> menus, long parentId){
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    //获取传入参数的子menu
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
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

