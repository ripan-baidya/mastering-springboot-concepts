package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // make sure add this annotation to use caching
public class SpringBootRedisApplication {

    static void main(String[] args) {
		SpringApplication.run(SpringBootRedisApplication.class, args);
	}

}
