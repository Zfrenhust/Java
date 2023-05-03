package com.renzf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.renzf.domain.ResponseResult;
import com.renzf.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-04-23 08:45:07
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);


    ResponseResult addComment(Comment comment);


}

