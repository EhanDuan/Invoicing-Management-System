package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.UserMapper;
import com.lzj.admin.pojo.User;
import com.lzj.admin.service.IUserService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Override
    public User login(java.lang.String userName, java.lang.String password) {
        // 如果用if判断参数时候合法等以及其他逻辑，则会引入大量的if判断
        // 这里引用了一个assertUtil 断言
        AssertUtil.isTrue(StringUtil.isEmpty(userName),"用户名不能为空！"); //如果程序出现异常，将会抛出自定义ParamsRuntime异常
        AssertUtil.isTrue(StringUtil.isEmpty(password),"密码不能为空！");
        // 根据用户名查询是否存在
        User user = this.findUserByUserName(userName);
        AssertUtil.isTrue(null == user, "该用户记录不存在或已注销");
        // 判断密码是否匹配
        // 后续会引入SpringSecurity 框架处理密码
        AssertUtil.isTrue(!user.getPassword().equals(password), "密码错误");
        return user;
    }

    @Override
    public User findUserByUserName(String userName) {
        // 使用mybatis plus来实现
        return this.baseMapper.selectOne(new QueryWrapper<User>().
                                        eq("is_del", 0).
                                        eq("user_name", userName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUseInfo(User user) {
        /**
         * 逻辑：
         * 1 用户名非空判断
         * 2 且更新时为唯一
         */
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUserName()), "用户名不能为空");
        User temp = this.findUserByUserName(user.getUserName());
        // 该用户名对应的用户不存在的情况下是合法的，因为是新的名字。
        // 如果找到了对应的用户，说明修改的是其他信息。分两种情况：(1). id相同 合法 （2）id不相同则 非法
        AssertUtil.isTrue( null != temp && !(temp.getId().equals(user.getId())), "用户名已存在"); //非法情况
        //更新
        AssertUtil.isTrue(!(this.updateById(user)), "用户信息更新失败！"); ;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword) {
        /**
         * 逻辑：
         * 1. 教研用户名非空，且必须存在
         * 2. 三个密码参数不能为空
         * 3. 原始密码必须正确
         * 4. 新密码与确认密码必须一致 并且不能与原始密码相同
         */
        User user = null;
        //从数据库获取对应的用户对象
        user = findUserByUserName(userName);
        //校验当前用户有没有登录,即第一条
        AssertUtil.isTrue(null == user, "用户不存在或未登录");
        // 校验原始密码非空
        AssertUtil.isTrue(StringUtil.isEmpty(oldPassword), "请输入原始密码");
        // 校验新密码非空
        AssertUtil.isTrue(StringUtil.isEmpty(newPassword), "请输入新密码");
        // 校验确认密码非空
        AssertUtil.isTrue(StringUtil.isEmpty(confirmPassword), "请输入确认密码");

        //校验原始密码match
        AssertUtil.isTrue(!(user.getPassword().equals(oldPassword)), "原始密码输入错误");
        //校验新密码和确认密码一致
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)), "新密码与确认密码输入不一致");
        // 校验新密码和原始密码是否一致
        AssertUtil.isTrue(newPassword.equals(oldPassword), "新密码与原始密码不能一致");

        //接下来做密码的更新
        user.setPassword(newPassword);
        AssertUtil.isTrue(!(this.updateById(user)), "用户密码更新失败");

    }
}
