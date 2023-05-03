package com.renzf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@MapperScan("com.renzf.mapper")
@EnableScheduling
@EnableSwagger2
public class ZFBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZFBlogApplication.class,args);
    }
}
