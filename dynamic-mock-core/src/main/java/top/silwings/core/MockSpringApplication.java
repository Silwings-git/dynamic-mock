package top.silwings.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @ClassName MockSpringApplication
 * @Description
 * @Author Silwings
 * @Date 2022/11/10 23:05
 * @Since
 **/
@EnableSwagger2WebMvc
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class MockSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockSpringApplication.class, args);
    }

}