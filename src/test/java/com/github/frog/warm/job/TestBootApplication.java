package com.github.frog.warm.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试主类
 *
 * @author tuzy
 */
@SpringBootApplication
public class TestBootApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestBootApplication.class);
        app.run(args);
    }
}
