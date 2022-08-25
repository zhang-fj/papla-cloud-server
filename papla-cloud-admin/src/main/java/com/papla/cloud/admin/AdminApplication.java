package com.papla.cloud.admin;

import com.papla.cloud.logging.client.LoggingFeignClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.papla.cloud.*"})
@MapperScan({"com.papla.cloud.**.mapper"})
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {LoggingFeignClient.class})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
