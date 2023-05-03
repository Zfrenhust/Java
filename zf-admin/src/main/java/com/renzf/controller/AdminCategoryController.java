package com.renzf.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.CategoryDto;
import com.renzf.domain.dto.UpdateCategoryDto;
import com.renzf.domain.entity.Category;
import com.renzf.domain.vo.AdminCategoryVo;
import com.renzf.domain.vo.ExcelCategoryVo;
import com.renzf.domain.vo.PageVo;
import com.renzf.enums.AppHttpCodeEnum;
import com.renzf.service.CategoryService;
import com.renzf.utils.BeanCopyUtils;
import com.renzf.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;

@RestController
@RequestMapping("/content/category")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<AdminCategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    //导出分类功能
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    //分页查询分类列表
    @GetMapping("/list")
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, @RequestParam(value="name",required = false) String name,@RequestParam(value="status",required = false) String status){
        return categoryService.listCategory(pageNum,pageSize,name,status);
    }

    //新增分类
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    //修改分类第一步:数据回显
    @GetMapping("/{id}")
    public ResponseResult displayCategory(@PathVariable("id") Long id){
        return categoryService.displayCategory(id);
    }
    //修改分类第二步:数据更新
    @PutMapping
    public ResponseResult updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto){
        return categoryService.updateCategory(updateCategoryDto);
    }

    //删除分类
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteCategory(id);
    }
}
