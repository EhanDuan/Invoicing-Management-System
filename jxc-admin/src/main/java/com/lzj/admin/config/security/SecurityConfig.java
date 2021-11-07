package com.lzj.admin.config.security;


import com.lzj.admin.filter.CaptchaCodeFilter;
import com.lzj.admin.pojo.User;
import com.lzj.admin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JxcAuthenticationSuccessHandler jxcAuthenticationSuccessHandler;

    @Autowired
    private JxcAuthenticationFailureHandler jxcAuthenticationFailureHandler;

    @Autowired
    private JxcLogoutSuccessHandler jxcLogoutSuccessHandler;

    @Resource
    private CaptchaCodeFilter captchaCodeFilter;

    @Resource
    private IUserService userService;

    @Resource
    private DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        //对静态资源进行放行
        web.ignoring().antMatchers("/css/**", "/error/**", "/images/**", "/js/**", "/lib/**");
    }


    //告诉springSecurity登录的页面以及登录成功的页面
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁止csrf 跨站攻击
        http.csrf().disable()
                .addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
                //允许iframe页面嵌套
                .headers().frameOptions().disable()
            .and()
                // 基于表单登录
                .formLogin()
                .usernameParameter("userName")
                .passwordParameter("password")
                .loginPage("/index")
                .loginProcessingUrl("/login") //框架自带的登录处理地址
                .successHandler(jxcAuthenticationSuccessHandler)//成功怎么办
                .failureHandler(jxcAuthenticationFailureHandler)//失败怎么办
            .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")
                .rememberMeCookieName("re")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .tokenRepository(persistentTokenRepository())
            .and()
                .logout()
                .logoutUrl("/signout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(jxcLogoutSuccessHandler)
            .and()
                .authorizeRequests().antMatchers( "/index","/login", "/image").permitAll()//认证匹配，即认证这几个页面的请求
                .anyRequest().authenticated();//对于其他的请求，做一个认证审查的处理
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }


    // 登录逻辑的实现,借助springSecurity,交给框架
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
                User userDetails = userService.findUserByUserName(userName);
                return userDetails;
            }
        };
    }

    // 对密码进行加密实现的方法(因为下面的认证的方法需要这个对象）
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    // 将userDetails交给框架
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(encoder());
    }
}
