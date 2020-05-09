package com.lmm.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableFeignClients(basePackages = {"xyz.staffjoy.account", "xyz.staffjoy.bot"})
@MapperScan({"com.lmm.base.mapper","com.lmm.base.dao"})
@SpringBootApplication
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }
}

