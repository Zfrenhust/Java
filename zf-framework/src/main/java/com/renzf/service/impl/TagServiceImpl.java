package com.renzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.constants.SystemConstants;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.TagListDto;
import com.renzf.domain.dto.TagUpdateDto;
import com.renzf.domain.entity.Tag;
import com.renzf.domain.vo.AdminTagVo;
import com.renzf.domain.vo.PageVo;
import com.renzf.domain.vo.TagVo;
import com.renzf.enums.AppHttpCodeEnum;
import com.renzf.exception.SystemException;
import com.renzf.mapper.TagMapper;
import com.renzf.service.TagService;
import com.renzf.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 21:29:45
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //新增标签
    @Override
    public ResponseResult addTag(Tag tag) {
        //标签不能为空
        if(!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NOT_NULL);
        }
        if(!StringUtils.hasText(tag.getRemark())){
            throw new SystemException(AppHttpCodeEnum.TAGREMARK_NOT_NULL);
        }
        //备注不能为空
        save(tag);
        return ResponseResult.okResult();
    }

    //删除标签
    @Override
    public ResponseResult deleteTag(Long id) {
//        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Tag::getId,id);
//        remove(queryWrapper);
        removeById(id);
        return ResponseResult.okResult();
    }

    //修改第一步:查询标签信息
    @Override
    public ResponseResult queryTag(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagUpdateDto tagUpdateDto) {
        Tag tag = getById(tagUpdateDto.getId());
        tag.setName(tagUpdateDto.getName());
        tag.setRemark(tagUpdateDto.getRemark());
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public List<AdminTagVo> getAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getDelFlag, SystemConstants.NOT_DELETE);
        List<Tag> tagList = list(queryWrapper);
        List<AdminTagVo> adminTagVos = BeanCopyUtils.copyBeanList(tagList, AdminTagVo.class);
        return adminTagVos;
    }


}

