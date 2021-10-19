package com.acme.poc.kafka.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApplicationConfig {

	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplicationBuilder(ApplicationConfig.class)
			//.profiles("kafka-string")
			.profiles("kafka-json")
			//.profiles("kafka-avro")
			.build();
		app.run();		
	}
}
