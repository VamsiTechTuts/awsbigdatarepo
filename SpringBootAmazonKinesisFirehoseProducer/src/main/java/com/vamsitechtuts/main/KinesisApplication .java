package com.vamsitechtuts.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.vamsitechtuts.controller"})
class KinesisApplication  {
    public static void main(String[] args) {
        SpringApplication.run(KinesisApplication.class, args);
    }
}
