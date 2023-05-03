package com.renzf.controller;


import com.renzf.domain.ResponseResult;
import com.renzf.domain.entity.Article;
import com.renzf.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    //返回热门文章
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        //查询热门文章 封装后返回
       ResponseResult result = articleService.hotArticleList();
       return result;
    }

    //返回文章列表
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    //获取全文内容
    @GetMapping("/{id}")
    public ResponseResult getArticleDetaile(@PathVariable("id") Long id){
        return articleService.getArticleDetaile(id);
    }

    //返回文章阅读数量
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
