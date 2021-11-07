package com.lzj.admin.controller;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.lzj.admin.model.CaptchaImageModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class CaptchaController {
    // 通过这个bean生成验证码
    @Resource
    public DefaultKaptcha defaultKaptcha;

    @RequestMapping(value="/image",method = RequestMethod.GET)
    public void kaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // 向浏览器输出文字
        // 要生成的验证码文字
        String capText = defaultKaptcha.createText();
        // 将验证码存入session 并设置2分钟后过期
        session.setAttribute("captcha_key",new CaptchaImageModel(capText,2*60));
        //借助输出流 把图片输出到浏览器
        ServletOutputStream out = response.getOutputStream();
        // 借助BufferedImage
        BufferedImage bufferedImage = defaultKaptcha.createImage(capText);
        ImageIO.write(bufferedImage,"jpg",out);
        //刷新缓冲
        out.flush();
    }
}
