package com.renzf.controller;

import com.renzf.domain.ResponseResult;
import com.renzf.domain.entity.Menu;
import com.renzf.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    //在菜单管理中 查询全部
    @GetMapping("/system/menu/list")
    public ResponseResult queryMenuList(@RequestParam(value="status",required = false) String status,@RequestParam(value="menuName",required = false) String menuName){
        return menuService.queryMenuList(status,menuName);
    }

    //新增菜单
    @PostMapping("/system/menu")
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    //更新菜单第一步:根据id查询菜单
    @GetMapping("/system/menu/{id}")
    public ResponseResult queryMenuById(@PathVariable Long id){
        return menuService.queryMenuById(id);
    }
    //更新菜单第二步:更新菜单
    @PutMapping("/system/menu")
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    //删除菜单
    @DeleteMapping("/system/menu/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long menuId){
        return menuService.deleteMenu(menuId);
    }


}
