package com.lzj.admin.interceptors;

import com.lzj.admin.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //会话中是否有用户信息
        User user = (User) request.getSession().getAttribute("user");

        if(null == user){
            //未登录
            response.sendRedirect("index");
            return false;
        }
        // 已登录，则放行
        return true;
    }
}
