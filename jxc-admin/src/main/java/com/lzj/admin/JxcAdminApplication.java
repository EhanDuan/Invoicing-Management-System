package com.lzj.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lzj.admin.mapper") //扫描持久层的接口
public class JxcAdminApplication {

    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123456"));
        SpringApplication.run(JxcAdminApplication.class, args);
    }
}
