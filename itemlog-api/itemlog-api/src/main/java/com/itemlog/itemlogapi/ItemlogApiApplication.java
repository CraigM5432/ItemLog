package com.itemlog.itemlogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Main entry point for the Spring Boot API.
// Starts the backend application.
@SpringBootApplication
public class ItemlogApiApplication {

    // Java main method called when the application starts.
    public static void main(String[] args) {

        // Starts the Spring Boot application and loads all configured beans,
        // controllers, services, repositories, security configuration, etc.
        SpringApplication.run(ItemlogApiApplication.class, args);
    }
}