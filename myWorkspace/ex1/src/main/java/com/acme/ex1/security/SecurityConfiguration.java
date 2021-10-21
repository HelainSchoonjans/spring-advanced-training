package com.acme.ex1.security;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // injected because of the Import annotation on ApplicationConfiguration
    private final PasswordEncoder passwordEncoder;

    SecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DataSource ds =
                new DriverManagerDataSource("jdbc:postgresql://localhost:5432/formation_spring", "postgres", null);
        /*
        valorisation des propriétés correspondant aux paramètres de connexion à la base
        de données.
        */
        auth.jdbcAuthentication()
                .dataSource(ds)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password, true from Member where username=?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?");
    }

}