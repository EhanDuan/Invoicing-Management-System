package com.lzj.admin.mapper;

import com.lzj.admin.pojo.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2021-11-06
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<String> findRolesByUserName(String userName);
}
