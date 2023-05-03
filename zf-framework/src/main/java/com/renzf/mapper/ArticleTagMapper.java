package com.renzf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renzf.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-26 16:28:56
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}

