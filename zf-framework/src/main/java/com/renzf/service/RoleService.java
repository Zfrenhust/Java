package com.renzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AddRoleDto;
import com.renzf.domain.dto.ChangeRoleDto;
import com.renzf.domain.dto.UpdateRoleDto;
import com.renzf.domain.entity.Role;
import com.renzf.domain.vo.PageVo;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-04-25 16:42:45
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult<PageVo> queryRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeRoleStatus(ChangeRoleDto changeRoleDto);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult roleDataDisplay(Long id);

    ResponseResult roleMenuTreeById(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRole(Long id);


    ResponseResult listAllRole();
}

