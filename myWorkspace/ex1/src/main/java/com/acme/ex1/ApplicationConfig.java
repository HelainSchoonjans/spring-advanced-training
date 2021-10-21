package com.acme.ex1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// this creates a bean of type BCryptPasswordEncoder of name 'bCryptPasswordEncoder'
@Import(BCryptPasswordEncoder.class)
@SpringBootApplication
public class ApplicationConfig {

    @Bean
    @Scope("prototype")
    Logger logger(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember()
                .getDeclaringClass());
    }

    public static void main(String[] args)  /*just to check if application context contains required beans*/ {
        var ctx = SpringApplication.run(ApplicationConfig.class, args);
        System.out.println("ctx is open : " + ctx);
        System.out.println(ctx.getBeansOfType(javax.sql.DataSource.class));
        System.out.println(ctx.getBeansOfType(javax.persistence.EntityManagerFactory.class));
        System.out.println(ctx.getBeansOfType(org.springframework.transaction.PlatformTransactionManager.class));

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

        /*
        @Component
        class A {
            @Transactional
            public void foo() {

            }
        }

        Que se passe t'il si on fait getClass sur une instance de A?
        - si on laisse @Transactional: Proxy
        - si on enlève le @Transactional: A
        En effet les annotations ajoutent des proxy autour de la classe.
         */
    }
}
