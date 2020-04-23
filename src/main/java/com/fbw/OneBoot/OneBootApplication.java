package com.fbw.OneBoot;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "com.fbw.OneBoot.mapper")
public class OneBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneBootApplication.class, args);
	}

}
