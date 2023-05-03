package com.renzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.constants.SystemConstants;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AddArticleDto;
import com.renzf.domain.dto.AdminArticleDetailDto;
import com.renzf.domain.dto.QueryArticleDto;
import com.renzf.domain.entity.Article;
import com.renzf.domain.entity.ArticleTag;
import com.renzf.domain.entity.Category;
import com.renzf.domain.vo.ArticleDetailVo;
import com.renzf.domain.vo.ArticleListVo;
import com.renzf.domain.vo.HotArticleVo;
import com.renzf.domain.vo.PageVo;
import com.renzf.mapper.ArticleMapper;
import com.renzf.mapper.ArticleTagMapper;
import com.renzf.service.ArticleService;
import com.renzf.service.ArticleTagService;
import com.renzf.service.CategoryService;
import com.renzf.utils.BeanCopyUtils;
import com.renzf.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(1,10);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();
        //Bean拷贝
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for(Article article :articles){
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);


        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //如果 有categoryId 就要 查询时要和传入相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        //查询categoryName
        List<Article> articles = page.getRecords();
        //articleId去查询articleName进行设置
//        for(Article article:articles){
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
        articles.stream()
                .map(article ->
                    article.setCategoryName(categoryService.getById(article.getCategoryId()).getName())
                );

        //封装结果查询
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }
    @Autowired
    private RedisCache redisCache;

    //根据id查询文章
    @Override
    public ResponseResult getArticleDetaile(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从rides中获取阅读量
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转成IO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long cateogryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(cateogryId);
        if(category != null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }


    //更新相应文章阅读数量
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新Redis中对应id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Autowired
    private ArticleTagService articleTagService;
    //后台管理系统新增文章
    @Override
    public ResponseResult add(AddArticleDto articleDto) {
        //增加博客
        Article article1 = BeanCopyUtils.copyBean(articleDto,Article.class);
        save(article1);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article1.getId(), tagId))
                .collect(Collectors.toList());
        //添加博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> pageArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getDelFlag,0);
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        List<QueryArticleDto> queryArticleDtos = BeanCopyUtils.copyBeanList(page.getRecords(), QueryArticleDto.class);
        PageVo pageVo = new PageVo(queryArticleDtos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public ResponseResult AdminGetArticleDetaile(Long id) {
        Article article = getById(id);
        AdminArticleDetailDto adminArticleDetailDto = BeanCopyUtils.copyBean(article, AdminArticleDetailDto.class);
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
        List tagList = new ArrayList();
        for (ArticleTag articleTag : articleTags) {
            tagList.add(articleTag.getTagId());
        }
        adminArticleDetailDto.setTags(tagList);
        return ResponseResult.okResult(adminArticleDetailDto);
    }

    @Override
    public ResponseResult articleUpdate(AdminArticleDetailDto adminArticleDetailDto) {
        Article article = BeanCopyUtils.copyBean(adminArticleDetailDto, Article.class);
        updateById(article);
        return ResponseResult.okResult();
    }

    //删除文章
    @Override
    public ResponseResult articleDelete(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
