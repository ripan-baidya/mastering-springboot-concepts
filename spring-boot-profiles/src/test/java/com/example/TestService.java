package com.example;

import com.example.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class TestService {
    /**
     * Profiles are also useful in testing. You can create a specific test profile and use it to run
     * tests with configurations different from production.
     *
     * The @ActiveProfiles("") annotation activates the "test" profile, ensuring that the test beans
     * and configurations are loaded.
     */
    private final DemoService demoService;

    @Autowired
    public TestService(DemoService demoService) {
        this.demoService = demoService;
    }

    @Test
    public void testMethod() {
        System.out.println("this is a test method.");
    }
}
