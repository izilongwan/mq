package com.share;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alicp.jetcache.anno.config.EnableMethodCache;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.share")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
