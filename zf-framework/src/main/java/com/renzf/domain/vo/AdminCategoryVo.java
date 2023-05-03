package com.renzf.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryVo {
    private Long id;
    private String name;
    //描述
    private String description;
}
