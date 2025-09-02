package com.ofss.stock_management_backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Server is up and running!";
    }
}