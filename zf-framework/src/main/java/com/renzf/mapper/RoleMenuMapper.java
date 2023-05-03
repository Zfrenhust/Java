package com.renzf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renzf.domain.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-28 12:16:05
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}

