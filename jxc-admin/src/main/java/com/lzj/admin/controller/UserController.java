package com.lzj.admin.controller;


import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.User;
import com.lzj.admin.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    //控制前端请求，客户登陆，前端假设使用ajax提交表单请求，后端响应json,如果登陆错误，则不用页面跳转

    // 对于 业务逻辑的控制， 通常是在service层，所以我们注入service层,注入接口
    @Resource
    private IUserService userService;

    @RequestMapping("login")
    @ResponseBody

    // 通常会把用户信息放到session里面，所以参数可以加一个session的参数来储存信息
//    public RespBean login(String userName, String password, HttpSession session){
//        try {
//            User user = userService.login(userName, password); //存一个user对象
//            // if login errors, we need to catch it
//            session.setAttribute("user", user); //放到session里面
//            return RespBean.success("用户登陆成功！");
//        } catch (ParamsException e) {
//            e.printStackTrace();
//            return RespBean.error(e.getMessage());
//        } catch (Exception e){
//            e.printStackTrace();
//            return RespBean.error("用户登录失败");
//        }
//    }
    public RespBean login(String userName, String password, HttpSession session){
        User user = userService.login(userName, password); //存一个user对象
        // if login errors, we need to catch it
        session.setAttribute("user", user); //放到session里面
        return RespBean.success("用户登陆成功！");
    }

    /**
     * 用户信息设置页面
     * @return
     */
    @RequestMapping("setting")
    public String setting(HttpSession session){
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", userService.getById(user.getId()));
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
    public RespBean updateUserPassword(HttpSession session, String oldPassword, String newPassword, String confirmPassword){
        try {
            User user = (User) session.getAttribute("user");
            userService.updateUserPassword(user.getUserName(), oldPassword, newPassword, confirmPassword);
            return RespBean.success("用户密码更新成功");
        } catch (ParamsException e) {
            e.printStackTrace();
            return RespBean.error(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("用户密码更新失败");
        }
    }

}
