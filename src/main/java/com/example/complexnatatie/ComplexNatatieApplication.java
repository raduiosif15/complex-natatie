package com.example.complexnatatie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ComplexNatatieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComplexNatatieApplication.class, args);
    }

}
