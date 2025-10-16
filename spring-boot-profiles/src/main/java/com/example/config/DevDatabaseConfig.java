package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class DevDatabaseConfig {
    /**
     * The @Profile annotation in Spring is used to mark beans or configurations that should only be
     * loaded when a specific profile is active.
     */
    @Bean
    // @Profile("dev") we can use @Profile("dev") to specify which bean would activate when dev profile is active
    public DataSource dataSource() {
        System.out.println("Using H2 database for development environment");
        return null;
    }
}
