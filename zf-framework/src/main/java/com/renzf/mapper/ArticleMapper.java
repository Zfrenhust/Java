package com.renzf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.renzf.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
