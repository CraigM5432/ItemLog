package com.itemlog.itemlogapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Public health-check endpoint used to confirm the backend API is running.
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "ItemLog API is working and running";
    }
}