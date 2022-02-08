package com.lzj.admin.mapper;

import com.lzj.admin.pojo.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2021-11-07
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Integer> queryRoleHasAllMenusByRoleId(Integer roleId);

    List<String> findAuthoritiesByRoleName(java.util.List<java.lang.String> roleNames);
}
