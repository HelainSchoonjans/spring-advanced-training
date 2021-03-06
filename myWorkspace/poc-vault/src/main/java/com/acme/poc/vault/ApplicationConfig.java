package com.acme.poc.vault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
public class ApplicationConfig {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(ApplicationConfig.class, args);
        // check we can reach vault
        System.out.println(ctx.getEnvironment().getProperty("api-key"));
		// we now have the database
        System.out.println(ctx.getBean(HikariDataSource.class));
        ctx.close();
    }
}
