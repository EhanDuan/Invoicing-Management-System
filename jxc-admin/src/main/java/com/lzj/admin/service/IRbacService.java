package com.lzj.admin.service;

import java.util.List;

public interface IRbacService {

    /**
     * 根据登录名查询分配的角色
     * @param userName
     * @return
     */
    List<String> findRolesByUserName(String userName);

    List<String> findAuthoritiesByRoleName(List<String> roleNames);
}
