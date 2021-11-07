package com.lzj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.User;
import com.lzj.admin.query.UserQuery;

import java.util.Map;

public interface IUserService extends IService<User> {
//    User login(String name, String password);

    //根据用户名查询用户记录
    public User findUserByUserName(String userName);

    void updateUseInfo(User user);

    void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword);

    Map<String, Object> userList(UserQuery userQuery);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Integer[] ids);
}
