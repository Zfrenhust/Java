package com.renzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AdminLinkDto;
import com.renzf.domain.entity.Link;
import com.renzf.domain.vo.AdminLinkVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-04-20 21:14:13
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult linkList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(AdminLinkDto adminLinkDto);

    ResponseResult displayLink(Long id);

    ResponseResult updateLink(AdminLinkVo adminLinkVo);

    ResponseResult deleteLink(Long id);
}

