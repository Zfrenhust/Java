package com.renzf.controller;

import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AddRoleDto;
import com.renzf.domain.dto.ChangeRoleDto;
import com.renzf.domain.dto.UpdateRoleDto;
import com.renzf.domain.vo.PageVo;
import com.renzf.service.MenuService;
import com.renzf.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    //查询角色 返回多个
    @GetMapping("/system/role/list")
    public ResponseResult<PageVo> queryRoleList(Integer pageNum, Integer pageSize,@RequestParam(value="roleName",required = false) String roleName,@RequestParam(value="status",required = false) String status){
        return roleService.queryRoleList(pageNum,pageSize,roleName,status);
    }

    //改变角色状态
    @PutMapping("/system/role/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody ChangeRoleDto changeRoleDto){
        return roleService.changeRoleStatus(changeRoleDto);
    }

    //新增角色:第一步获取所有菜单选项 供用户选择
    @GetMapping("/system/menu/treeselect")
    public ResponseResult queryRoleTree(){
        return menuService.queryRoleTree();
    }
    //新增角色:第二步新增
    @PostMapping("/system/role")
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    //修改角色:第一步数据回显
    @GetMapping("/system/role/{id}")
    public ResponseResult roleDataDisplay(@PathVariable("id") Long id){
        return roleService.roleDataDisplay(id);
    }
    //修改角色:第二步加载角色所关联的菜单权限id列表
    @GetMapping("/system/menu/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeById(@PathVariable("id") Long id) {
        return roleService.roleMenuTreeById(id);
    }
    //修改角色:第三步更新信息
    @PutMapping("/system/role")
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }

    //删除角色
    @DeleteMapping("/system/role/{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long id){
        return roleService.deleteRole(id);
    }
}
