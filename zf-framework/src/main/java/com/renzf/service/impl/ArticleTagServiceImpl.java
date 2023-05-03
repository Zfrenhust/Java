package com.renzf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.domain.entity.ArticleTag;
import com.renzf.mapper.ArticleTagMapper;
import com.renzf.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-04-26 16:28:58
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

