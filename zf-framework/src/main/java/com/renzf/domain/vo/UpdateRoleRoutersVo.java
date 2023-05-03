package com.renzf.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRoutersVo {
    private List<RoleMenuVo> menus;
    private List<String> checkedKeys;
}
