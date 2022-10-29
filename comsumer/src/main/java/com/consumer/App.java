package com.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.alicp.jetcache.anno.config.EnableMethodCache;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.consumer")
@ComponentScan({ "com.share", "com.consumer" })
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
