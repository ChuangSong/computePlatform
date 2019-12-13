package com.bdilab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bdilab.mapper")
public class ComputingplatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComputingplatformApplication.class, args);
    }

}
