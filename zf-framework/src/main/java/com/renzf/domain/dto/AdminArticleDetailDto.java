package com.renzf.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminArticleDetailDto {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;

    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;
    //问章内容
    private String content;

    private Long updateBy;

    private Date updateTime;

    private Long createBy;

    private Date createTime;

    private Integer delFlag;

    private List tags;
    private String isComment;
    private String isTop;
    private String status;

}
