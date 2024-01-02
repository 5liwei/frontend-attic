package com.frontendAttic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName FrontendAtticAdminApplication
 * @Description TODO
 * @Author LiWei
 * @Date 2024/01/02 19:39
 */
@SpringBootApplication(scanBasePackages = {"com.frontendAttic"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class FrontendAtticApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontendAtticApiApplication.class, args);
    }
}
