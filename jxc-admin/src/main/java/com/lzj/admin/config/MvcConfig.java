package com.lzj.admin.config;


import com.lzj.admin.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //借助registry
        registry.addInterceptor(noLoginInterceptor())
                .addPathPatterns("/**") // 拦截所有页
                .excludePathPatterns("/index", "/user/login", "/css/**", "/error/**",
                                        "/images/**", "/js/**","/lib/**"); //放行登录页和登录的接口，以及静态资源地址
    }
}
