package com.renzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.renzf.constants.SystemConstants;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AdminLinkDto;
import com.renzf.domain.entity.Link;
import com.renzf.domain.vo.AdminLinkVo;
import com.renzf.domain.vo.LinkVo;
import com.renzf.domain.vo.PageVo;
import com.renzf.mapper.LinkMapper;
import com.renzf.service.LinkService;
import com.renzf.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-04-20 21:14:13
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    //查询所有审核通过的友链
    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);//友链审核通过
        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    //在管理系统中查询所有友链
    @Override
    public ResponseResult linkList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getDelFlag,SystemConstants.NOT_DELETE);
        queryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        queryWrapper.like(StringUtils.hasText(status),Link::getStatus,status);
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<AdminLinkVo> adminLinkVos = BeanCopyUtils.copyBeanList(page.getRecords(), AdminLinkVo.class);
        return ResponseResult.okResult(new PageVo(adminLinkVos,page.getTotal()));
    }

    //新增友链
    @Override
    public ResponseResult addLink(AdminLinkDto adminLinkDto) {
        Link link = BeanCopyUtils.copyBean(adminLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult displayLink(Long id) {
        Link link = getById(id);
        AdminLinkVo adminLinkVo = BeanCopyUtils.copyBean(link, AdminLinkVo.class);
        return ResponseResult.okResult(adminLinkVo);
    }

    @Override
    public ResponseResult updateLink(AdminLinkVo adminLinkVo) {
        Link link = BeanCopyUtils.copyBean(adminLinkVo, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}

