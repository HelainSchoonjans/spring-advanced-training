package com.acme.ex2;

import javax.annotation.PostConstruct;
import javax.persistence.Cacheable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class ApplicationConfig {
	
    @Autowired
    private EntityManagerFactory emf;


	@Bean
	@Scope("prototype")
	Logger logger(InjectionPoint ip) {
		return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
	}
	
    @PostConstruct
    public void loadCache() {
        EntityManager em = emf.createEntityManager();
        emf.getMetamodel().getEntities().stream()
            .filter(e -> e.getJavaType().isAnnotationPresent(Cacheable.class))
            .map(e -> "select e from "+e.getName()+" e")
            .forEach(q -> em.createQuery(q).getResultList());
        em.close();
    }
    
    public static void main(String[] args) {
		SpringApplication.run(ApplicationConfig.class, args);
	}
}