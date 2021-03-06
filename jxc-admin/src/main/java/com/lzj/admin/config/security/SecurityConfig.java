package com.lzj.admin.config.security;


import com.lzj.admin.config.ClassPathTldsLoader;
import com.lzj.admin.filter.CaptchaCodeFilter;
import com.lzj.admin.pojo.User;
import com.lzj.admin.service.IRbacService;
import com.lzj.admin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
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
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private IRbacService rbacService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        //???????????????????????????
        web.ignoring().antMatchers("/css/**", "/error/**", "/images/**", "/js/**", "/lib/**");
    }


    //??????springSecurity??????????????????????????????????????????
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //??????csrf ????????????
        http.csrf().disable()
                .addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
                //??????iframe????????????
                .headers().frameOptions().disable()
            .and()
                // ??????????????????
                .formLogin()
                .usernameParameter("userName")
                .passwordParameter("password")
                .loginPage("/index")
                .loginProcessingUrl("/login") //?????????????????????????????????
                .successHandler(jxcAuthenticationSuccessHandler)//???????????????
                .failureHandler(jxcAuthenticationFailureHandler)//???????????????
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
                .authorizeRequests().antMatchers( "/index","/login", "/image").permitAll()//????????????????????????????????????????????????
                .anyRequest().authenticated();//??????????????????????????????????????????????????????
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }


    // ?????????????????????,??????springSecurity,????????????
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User userDetails = userService.findUserByUserName(username);
                /**
                 * 1??????????????????????????????
                 * 2???????????????????????????????????????????????????????????????
                 */
                List<String> roleNames = rbacService.findRolesByUserName(username);
                List<String> authorities = rbacService.findAuthoritiesByRoleName(roleNames);

                // ????????????roleNames????????????"ROLE"??????????????????springSecurity?????????
                roleNames = roleNames.stream().map(role ->
                    "ROLE" + role
                ).collect(Collectors.toList());

                authorities.addAll(roleNames);
                userDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authorities)));
                return userDetails;
            }
        };
    }

    // ????????????????????????????????????(???????????????????????????????????????????????????
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    // ???userDetails????????????
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(encoder());
    }


    /**
     * ??????ClassPathTldsLoader
     * @return
     */

    @Bean
    @ConditionalOnBean(ClassPathTldsLoader.class)
    public ClassPathTldsLoader classPathTldsLoader(){
        return new ClassPathTldsLoader();
    }
}
