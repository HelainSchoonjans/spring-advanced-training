package com.acme.poc.vault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationConfig {

	public static void main(String[] args) {
		var ctx = SpringApplication.run(ApplicationConfig.class, args);	
		ctx.close();
	}
}
