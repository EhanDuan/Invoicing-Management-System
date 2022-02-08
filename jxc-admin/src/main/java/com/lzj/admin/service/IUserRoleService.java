package com.lzj.admin.service;

import com.lzj.admin.pojo.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2021-11-06
 */
public interface IUserRoleService extends IService<UserRole> {

    List<String> findRolesByUserName(String userName);
}
