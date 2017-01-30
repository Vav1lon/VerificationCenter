package ru.vav1lon.verificationCenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VerificationCenterApplication {

    public static void main(String[] args) {
        SpringApplication
                .run(VerificationCenterApplication.class, args)
                .registerShutdownHook();
    }
}
