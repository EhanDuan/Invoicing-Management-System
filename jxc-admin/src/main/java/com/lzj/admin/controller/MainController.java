package com.lzj.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    /**
     * system login index view
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    /**
     * system main view
     * @return
     */
    @RequestMapping("main")
    public String main(){
        return "main";
    }

    /**
     * system welcome view
     * @return
     */
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 用户推出
     * @return
     */
    @RequestMapping("signout")
    public String signout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:index";
    }


}
