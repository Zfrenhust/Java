package com.renzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AddArticleDto;
import com.renzf.domain.dto.AdminArticleDetailDto;
import com.renzf.domain.entity.Article;
import com.renzf.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetaile(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult<PageVo> pageArticleList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult AdminGetArticleDetaile(Long id);

    ResponseResult articleUpdate(AdminArticleDetailDto adminArticleDetailDto);

    ResponseResult articleDelete(Long id);
}
