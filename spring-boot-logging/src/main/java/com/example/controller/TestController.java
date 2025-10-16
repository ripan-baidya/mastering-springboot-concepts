package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/tests")
public class TestController {
    // Creating a logger instance
    Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/")
    public ResponseEntity<String> getTestResponse() {
        log.error("ERROR log");
        log.warn("WARN log");
        log.info("INFO log");
        log.debug("DEBUG log");
        log.trace("TRACE log");

        /**
         * ERROR > WARN > INFO > DEBUG > TRACE
         * If we access the endpoint: http://localhost:8080/api/v1/tests/
         * and if we never mention any specific log levels the by default We will only see ERROR, WARN,
         * INFO logs. To enable TRACE and DEBUG logs, we need to configure the log levels explicitly.
         */
        return ResponseEntity.ok("Test Response");
    }
}
