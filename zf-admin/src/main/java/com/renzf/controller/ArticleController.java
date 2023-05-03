package com.renzf.controller;

import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AddArticleDto;
import com.renzf.domain.dto.AdminArticleDetailDto;
import com.renzf.domain.vo.PageVo;
import com.renzf.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }

    @GetMapping("/list")
    //获取文章列表
    public ResponseResult<PageVo> articleList(Integer pageNum, Integer pageSize, String title, String summary){
        return articleService.pageArticleList(pageNum,pageSize,title,summary);
    }

    //修改文章第一步:查询文章详情
    @GetMapping("/{id}")
    public ResponseResult articleDetail(@PathVariable("id") Long id){
        return articleService.AdminGetArticleDetaile(id);
    }
    //修改文章第二步:更新文章
    @PutMapping
    public ResponseResult articleUpdate(@RequestBody AdminArticleDetailDto adminArticleDetailDto){
        return articleService.articleUpdate(adminArticleDetailDto);
    }

    //删除文章
    @DeleteMapping("/{id}")
    public ResponseResult articleDelete(@PathVariable("id") Long id){
        return articleService.articleDelete(id);
    }
}
