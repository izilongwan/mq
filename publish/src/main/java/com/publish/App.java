package com.publish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.alicp.jetcache.anno.config.EnableMethodCache;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.publish")
@ComponentScan({ "com.share", "com.publish" })
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
