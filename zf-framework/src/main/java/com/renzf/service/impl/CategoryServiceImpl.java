package com.renzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.constants.SystemConstants;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.CategoryDto;
import com.renzf.domain.dto.UpdateCategoryDto;
import com.renzf.domain.entity.Article;
import com.renzf.domain.entity.Category;
import com.renzf.domain.vo.AdminCategoryFunctionVo;
import com.renzf.domain.vo.AdminCategoryVo;
import com.renzf.domain.vo.CategoryVo;
import com.renzf.domain.vo.PageVo;
import com.renzf.mapper.CategoryMapper;
import com.renzf.service.ArticleService;
import com.renzf.service.CategoryService;
import com.renzf.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-04-19 15:26:37
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表 状态为已发布
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id,并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories.stream().filter(category ->SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    //管理系统中写博文是用来查询分类
    @Override
    public List<AdminCategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<AdminCategoryVo> adminCategoryVos = BeanCopyUtils.copyBeanList(list,AdminCategoryVo.class);
        return adminCategoryVos;
    }

    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.like(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<AdminCategoryFunctionVo> adminCategoryFunctionVos = BeanCopyUtils.copyBeanList(page.getRecords(), AdminCategoryFunctionVo.class);
        return ResponseResult.okResult(new PageVo(adminCategoryFunctionVos,page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult displayCategory(Long id) {
        Category category = getById(id);
        return ResponseResult.okResult(category);
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto) {
        Category category = BeanCopyUtils.copyBean(updateCategoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}

