package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("default")
public class DefaultConfig {
    /**
     * Spring provides a default profile that is active if no specific profile is set. You can define
     * this fallback configuration for cases where a profile is not specified.
     */
    @Bean
    public DataSource dataSource() {
        System.out.println("Using Default database configuration");
        return null;
    }
}
