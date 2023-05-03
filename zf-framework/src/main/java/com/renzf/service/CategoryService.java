package com.renzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.CategoryDto;
import com.renzf.domain.dto.UpdateCategoryDto;
import com.renzf.domain.entity.Category;
import com.renzf.domain.vo.AdminCategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-04-19 15:26:36
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<AdminCategoryVo> listAllCategory();

    ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult displayCategory(Long id);

    ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto);

    ResponseResult deleteCategory(Long id);
}

