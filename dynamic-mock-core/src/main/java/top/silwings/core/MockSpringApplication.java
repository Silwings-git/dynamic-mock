package top.silwings.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName MockSpringApplication
 * @Description
 * @Author Silwings
 * @Date 2022/11/10 23:05
 * @Since
 **/
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class MockSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockSpringApplication.class, args);
    }

}