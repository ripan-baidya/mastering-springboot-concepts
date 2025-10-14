package com.orbitdynamix.controller;

import com.orbitdynamix.dto.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // test api
    @GetMapping("/")
    private ResponseEntity<ApiResponse<String>> getApiResponse() {
        var response = new ApiResponse<String>(true, "Hello Test Controller", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
