package com.pi.dorossai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class DorossaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DorossaiApplication.class, args);
    }

}
