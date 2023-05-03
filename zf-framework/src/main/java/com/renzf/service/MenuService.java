package com.renzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-04-25 16:35:41
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPeremsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);


    ResponseResult queryMenuById(Long id);

    ResponseResult queryMenuList(String status, String menuName);

    ResponseResult updateMenu(Menu menu);

    ResponseResult addMenu(Menu menu);

    ResponseResult deleteMenu(Long menuId);


    ResponseResult queryRoleTree();


}

