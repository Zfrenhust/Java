package com.renzf.controller;

import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.TagListDto;
import com.renzf.domain.dto.TagUpdateDto;
import com.renzf.domain.entity.Tag;
import com.renzf.domain.vo.AdminTagVo;
import com.renzf.domain.vo.PageVo;
import com.renzf.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    //获取标签
    @GetMapping("/content/tag/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    //新增标签
    @PostMapping("/content/tag")
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    //删除标签
    @DeleteMapping("/content/tag/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }

    //修改第一步:查询标签信息
    @GetMapping("content/tag/{id}")
    public ResponseResult queryTag(@PathVariable("id") Long id){
        return tagService.queryTag(id);
    }
    //修改第二步:修改标签信息
    @PutMapping("content/tag")
    public ResponseResult updateTag(@RequestBody TagUpdateDto tagUpdateDto){
        return tagService.updateTag(tagUpdateDto);
    }

    //写博文功能中 调用文章所有标签的接口
    @GetMapping("/content/tag/listAllTag")
    public ResponseResult getAllTag(){
        List<AdminTagVo> adminTagVoList = tagService.getAllTag();
        return ResponseResult.okResult(adminTagVoList);
    }
}
