package com.acme.ex1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class ApplicationConfig {

    @Bean
    @Scope("prototype")
    Logger logger(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
    }

    public static void main(String[] args)  /*just to check if application context contains required beans*/ {
        var ctx = SpringApplication.run(ApplicationConfig.class, args);
        System.out.println("ctx is open : " + ctx);
        System.out.println(ctx.getBeansOfType(javax.sql.DataSource.class));
        System.out.println(ctx.getBeansOfType(javax.persistence.EntityManagerFactory.class));
        //System.out.println(ctx.getBeansOfType(org.springframework.transaction.PlatformTransactionManager.class));
        System.out.println("closing");
        ctx.close();

        // spring is collecting the factories:
        // ApplicationConfig.class.getClassLoader().getResources("META_INF/")
        /*
        while(resources.hasMoreElements()) {
           Url url = resources.nextElement();
           try (InputSteam is = url.openStream(){
                Properties props = new Properties();
                if(props.containsKey(EnableAutoConfiguration.class.getName())) {
                    String classesToLoad = props.getProperty(EnableAutoConfiguration.class.getName();
                    }
            }
        }
        }*/
    }
}
