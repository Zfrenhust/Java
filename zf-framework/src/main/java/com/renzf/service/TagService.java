package com.renzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.TagListDto;
import com.renzf.domain.dto.TagUpdateDto;
import com.renzf.domain.entity.Tag;
import com.renzf.domain.vo.AdminTagVo;
import com.renzf.domain.vo.PageVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-04-24 21:29:45
 */
public interface TagService  extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult queryTag(Long id);


    ResponseResult updateTag(TagUpdateDto tagUpdateDto);

    List<AdminTagVo> getAllTag();
}

