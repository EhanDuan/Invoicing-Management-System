package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.UserMapper;
import com.lzj.admin.pojo.User;
import com.lzj.admin.pojo.UserRole;
import com.lzj.admin.query.UserQuery;
import com.lzj.admin.service.IUserRoleService;
import com.lzj.admin.service.IUserService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IUserRoleService userRoleService;
//    @Override
//    public User login(java.lang.String userName, java.lang.String password) {
//        // 如果用if判断参数时候合法等以及其他逻辑，则会引入大量的if判断
//        // 这里引用了一个assertUtil 断言
//        AssertUtil.isTrue(StringUtil.isEmpty(userName),"用户名不能为空！"); //如果程序出现异常，将会抛出自定义ParamsRuntime异常
//        AssertUtil.isTrue(StringUtil.isEmpty(password),"密码不能为空！");
//        // 根据用户名查询是否存在
//        User user = this.findUserByUserName(userName);
//        AssertUtil.isTrue(null == user, "该用户记录不存在或已注销");
//        // 判断密码是否匹配
//        // 后续会引入SpringSecurity 框架处理密码
//        AssertUtil.isTrue(!user.getPassword().equals(password), "密码错误");
//        return user;
//    }

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
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUsername()), "用户名不能为空");
        User temp = this.findUserByUserName(user.getUsername());
        // 该用户名对应的用户不存在的情况下是合法的，因为是新的名字。
        // 如果找到了对应的用户，说明修改的是其他信息。分两种情况：(1). id相同 合法是他自己 （2）id不相同和别人名字一样 则 非法
        AssertUtil.isTrue( null != temp && !(temp.getId().equals(user.getId())), "用户名已存在"); //非法情况
        //更新
        AssertUtil.isTrue(!(this.updateById(user)), "用户信息更新失败！");
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword) {
        /**
         * 逻辑：
         * 1. 校验用户名非空，且必须存在
         * 2. 三个密码参数不能为空
         * 3. 原始密码必须正确
         * 4. 新密码与确认密码必须一致 并且不能与原始密码相同
         */
        User user = null;
        //从数据库获取对应的用户对象
        user = findUserByUserName(userName);
        //校验当前用户有没有登录,即第一条
        AssertUtil.isTrue(null == user, "用户不存在或未登录！");
        // 校验原始密码非空
        AssertUtil.isTrue(StringUtil.isEmpty(oldPassword), "请输入原始密码！");
        // 校验新密码非空
        AssertUtil.isTrue(StringUtil.isEmpty(newPassword), "请输入新密码！");
        // 校验确认密码非空
        AssertUtil.isTrue(StringUtil.isEmpty(confirmPassword), "请输入确认密码！");

        //校验原始密码match （明文密码，加密密码）
        AssertUtil.isTrue(!(passwordEncoder.matches(oldPassword, user.getPassword())), "原始密码输入错误！");
        //校验新密码和确认密码一致
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)), "新密码与确认密码输入不一致！");
        // 校验新密码和原始密码是否一致
        AssertUtil.isTrue(newPassword.equals(oldPassword), "新密码与原始密码不能一致！");

        //接下来做密码的更新
        //这里要存秘文
        user.setPassword(passwordEncoder.encode(newPassword));
        AssertUtil.isTrue(!(this.updateById(user)), "用户密码更新失败！");
    }

    @Override
    public Map<String, Object> userList(UserQuery userQuery) {
        IPage<User> page = new Page<>(userQuery.getPage(), userQuery.getLimit());//当前页数和每页大小
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        if(StringUtils.isNotBlank(userQuery.getUserName())){
            queryWrapper.like("user_name", userQuery.getUserName());
        }


        page = this.baseMapper.selectPage(page, queryWrapper);
        //多条件分页查询结束了，准备相应的结果
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", 0);
//        map.put("msg", "");
//        map.put("data", page.getRecords()); // 当前页列表记录
//        map.put("count", page.getTotal());
//        return map;
    }

    /**
     * 用户记录添加接口
     * @param user
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUser(User user) {
        /**
         * 用户名非空
         * 用户名不能重复
         * 用户密码默认设置为123456
         * 用户默认有效
         */

        AssertUtil.isTrue(StringUtils.isBlank(user.getUsername()), "用户名不能为空");
        System.out.println(null != this.findUserByUserName(user.getUsername()));
        AssertUtil.isTrue(null != this.findUserByUserName(user.getUsername()), "用户名已存在");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setIsDel(0);
        AssertUtil.isTrue(!(this.save(user)), "用户记录添加失败");
        // 重新查询用户记录，上面的方法返回的不是主键
        User temp = this.findUserByUserName(user.getUsername());
        /**
         * 给用户分配角色
         */
        relationUserRole(temp.getId(), user.getRoleIds());
    }

    /**
     * 关联用户和角色关系, 考虑添加与更新
     * @param userId
     * @param roleIds
     */
    private void relationUserRole(Integer userId, String roleIds) {
        // 核心表 t_user_role
        // 添加： 如果角色记录存在，则执行批量添加即可
        // 更新： 如果存在原始角色记录，直接删除原来的角色记录，重新添加新的用户角色记录
        // 更新： 如果不存在，直接执行添加即可
        // 实现思路：（1）查询用户的原始分配角色
        //         （2）如果存在原始用户角色记录，根据用户id删除， 然后重新添加新的用户角色记录
        //         （3）不存在，则执行批量增加即可
        int count = userRoleService.count(new QueryWrapper<UserRole>().eq("user_id", userId));
        if(count > 0){
            // 说明存在原始的角色用户记录
            // 删除掉
            AssertUtil.isTrue(!(userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId))), "用户角色分配失败");
        }
        // 判断现在更新的角色id非空
        if(StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoles = new ArrayList<>();
            // 1, 2, 3, 4 role format
            for(String s: roleIds.split(",")){
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(s));
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(!userRoleService.saveBatch(userRoles), "用户角色分配失败");
        }

    }

    /**
     * 用户记录更新接口
     * @param user
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUser(User user) {
        /**
         * 用户名非空
         * 用户名不能重复
         */
        AssertUtil.isTrue(StringUtils.isBlank(user.getUsername()), "用户名不能为空");
        User temp = this.findUserByUserName(user.getUsername());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(user.getId())), "用户名已存在");
        relationUserRole(user.getId(), user.getRoleIds());
        AssertUtil.isTrue(!(this.updateById(user)), "用户记录更新失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    // 删除用户的时候也需要删除角色信息，先删除从表，再删除主表
    public void deleteUser(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除的记录id");

        int count = userRoleService.count(new QueryWrapper<UserRole>().in("user_id", Arrays.asList(ids))); //  查询可能多个用户的角色
        // 从表删除数据
        if(count > 0){
            AssertUtil.isTrue(!(userRoleService.remove(new QueryWrapper<UserRole>().in("user_id", Arrays.asList(ids)))),"用户记录删除失败");
        }
        // 主表删除数据
        List<User> users = new ArrayList<>();
        for(Integer id: ids){
            User temp = this.getById(id);
            temp.setIsDel(1); //logic delete
            users.add(temp);
        }
         AssertUtil.isTrue(!(this.updateBatchById(users)), "用户记录删除失败"); ;

    }
}
