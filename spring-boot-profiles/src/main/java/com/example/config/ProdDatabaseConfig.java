package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class ProdDatabaseConfig {
    /**
     * The @Profile annotation in Spring is used to mark beans or configurations that should only be
     * loaded when a specific profile is active.
     */
    @Bean
    // @Profile("prod") we can use @Profile("dev") to specify which bean would activate when dev profile is active
    public DataSource dataSource() {
        System.out.println("Using PostgreSQL database for development environment");
        return null;
    }
}
