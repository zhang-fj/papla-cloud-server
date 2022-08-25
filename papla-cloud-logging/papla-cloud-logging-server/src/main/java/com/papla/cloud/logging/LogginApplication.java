package com.papla.cloud.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.papla.cloud.*"})
@EnableDiscoveryClient
public class LogginApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogginApplication.class, args);
    }

}
