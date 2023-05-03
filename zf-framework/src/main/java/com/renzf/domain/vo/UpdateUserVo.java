package com.renzf.domain.vo;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserVo {
    private List<String> roleIds;
    private List<UpdateUserRoleVo> roles;
    private UserVo user;
}
