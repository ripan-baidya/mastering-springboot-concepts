package com.example.controller;

import com.example.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class TestJwtProperties {

    private final JwtProperties jwtProperties;

    @Autowired
    public TestJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @GetMapping("/jwt")
    public ResponseEntity<String> testJwtProperties() {
        return ResponseEntity.ok("JWT Properties: " + jwtProperties.getSecret() + " " + jwtProperties.getExpiration());
    }
}
