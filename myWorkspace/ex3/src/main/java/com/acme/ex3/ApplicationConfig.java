package com.acme.ex3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import io.r2dbc.spi.ConnectionFactory;


@SpringBootApplication
@EnableReactiveMethodSecurity
public class ApplicationConfig {

	@Bean @Scope("prototype")
	public Logger logger(InjectionPoint ip) {
		return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
	}
	
	@Bean
	SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
	    return http
	    		.oauth2ResourceServer(x -> x.jwt())
	    		.csrf(x -> x.disable())
	      .build();
	}

	public static void main(String[] args) {
		var ctx = SpringApplication.run(ApplicationConfig.class, args);
		System.out.println(ctx.getBeansOfType(ConnectionFactory.class));
		System.out.println(ctx.getBeansOfType(ReactiveElasticsearchClient.class));
		System.out.println(ctx.getBeansOfType(ReactiveElasticsearchTemplate.class));
	}
}
