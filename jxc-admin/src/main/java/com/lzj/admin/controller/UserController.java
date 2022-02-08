package com.lzj.admin.controller;


import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.User;
import com.lzj.admin.query.UserQuery;
import com.lzj.admin.service.IUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    //控制前端请求，客户登陆，前端假设使用ajax提交表单请求，后端响应json,如果登陆错误，则不用页面跳转

    // 对于 业务逻辑的控制， 通常是在service层，所以我们注入service层,注入接口
    @Resource
    private IUserService userService;

    /**
     * 用户信息设置页面
     * @return
     */
    // TODO C1P71923
    @RequestMapping("setting")
    public String setting(Principal principal, Model model){
        User user = userService.findUserByUserName(principal.getName());
        model.addAttribute("user", user);
        return "user/setting";
    }

    /**
     * 用户信息更新
     * @param user
     * @return
     */
    @RequestMapping("updateUserInfo")
    @ResponseBody // 默认返回json字符串
    public RespBean updateUserInfo(User user){
        try {
            userService.updateUseInfo(user);
            return RespBean.success("用户信息更新成功");
        } catch (ParamsException e) {
            e.printStackTrace();
            return RespBean.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("用户信息更新失败");
        }
    }

    /**
     * 用户密码更新页
     * @return
     */
    @RequestMapping("password")
    public String password(){
        return "user/password";
    }

    @RequestMapping("updateUserPassword")
    @ResponseBody
    //前端提供过来有三个参数：原始密码，新密码，确认密码
    public RespBean updateUserPassword(Principal principal, String oldPassword, String newPassword, String confirmPassword){

        userService.updateUserPassword(principal.getName(), oldPassword, newPassword, confirmPassword);
        return RespBean.success("用户密码更新成功");

    }

    /**
     * 用户列表查询接口
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @PreAuthorize("hasAnyAuthority('101003')") // 基于注解的权限控制，内括号输入对应的权限val
    @ResponseBody
    public Map<String, Object> userList(UserQuery userQuery){
        return userService.userList(userQuery);
    }

    /**
     * 添加｜更新用户页面 （转发视图Model）
     * @param id
     * @param model
     * @return
     */

    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdatePage(Integer id, Model model){
        if(null != id){ // update
            model.addAttribute("user", userService.getById(id)) ;
        }

        return "user/add_update";
    }

    /**
     * 用户记录添加接口
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveUser(User user){
        userService.saveUser(user);
        return RespBean.success("用户记录添加成功");
    }

    /**
     * 用户记录更新接口
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateUser(User user){
        userService.updateUser(user);
        return RespBean.success("用户记录更新成功");
    }

    /**
     * 用户记录删除接口
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return RespBean.success("用户记录删除成功");
    }

    @RequestMapping("index")
    @PreAuthorize("hasAnyAuthority('1010')") // 基于注解的权限控制，内括号输入对应的权限val
    public String index(){
        return "user/user";
    }

}
