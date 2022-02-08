package com.lzj.admin.service;

import com.lzj.admin.pojo.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2021-11-07
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    List<Integer> queryRoleHasAllMenusByRoleId(Integer roleId);

    List<String> findAuthoritiesByRoleName(java.util.List<java.lang.String> roleNames);
}
