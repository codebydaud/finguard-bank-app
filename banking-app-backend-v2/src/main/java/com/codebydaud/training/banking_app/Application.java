package com.codebydaud.training.banking_app;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("DB_PROD_USERNAME", dotenv.get("DB_PROD_USERNAME"));
        System.setProperty("DB_PROD_PASSWORD", dotenv.get("DB_PROD_PASSWORD"));

        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));

        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));

        SpringApplication.run(Application.class, args);
    }

}