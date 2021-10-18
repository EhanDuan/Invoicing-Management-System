package com.lzj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.User;

public interface IUserService extends IService<User> {
    User login(String name, String password);

    //根据用户名查询用户记录
    public User findUserByUserName(String userName);

    void updateUseInfo(User user);

    void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword);
}
