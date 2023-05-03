package com.renzf.controller;

import com.renzf.domain.ResponseResult;
import com.renzf.domain.dto.AdminLinkDto;
import com.renzf.domain.vo.AdminLinkVo;
import com.renzf.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LinkController {

    @Autowired
    private LinkService linkService;

    //获取所有友链
    @GetMapping("/content/link/list")
    public ResponseResult linkList(Integer pageNum, Integer pageSize, @RequestParam(value="name",required = false) String name,@RequestParam(value="status",required = false) String status){
        return linkService.linkList(pageNum,pageSize,name,status);
    }

    //新增友链
    @PostMapping("/content/link")
    public ResponseResult addLink(@RequestBody AdminLinkDto adminLinkDto){
        return linkService.addLink(adminLinkDto);
    }

    //修改友链第一步:数据回显
    @GetMapping("/content/link/{id}")
    public ResponseResult displayLink(@PathVariable Long id){
        return linkService.displayLink(id);
    }
    //修改友链第二步:更新信息
    @PutMapping("/content/link")
    public ResponseResult updateLink(@RequestBody AdminLinkVo adminLinkVo){
        return linkService.updateLink(adminLinkVo);
    }

    //删除友链
    @DeleteMapping("/content/link/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }
}
