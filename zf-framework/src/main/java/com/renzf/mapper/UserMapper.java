package com.renzf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renzf.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-21 09:36:22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

