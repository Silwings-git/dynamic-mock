package top.silwings.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName MockSpringApplication
 * @Description
 * @Author Silwings
 * @Date 2022/11/10 23:05
 * @Since
 **/
@SpringBootApplication
public class MockSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockSpringApplication.class, args);
    }
// TODO_Silwings: 2023/1/4 减少日志对性能的影响,任务循环依赖,基准测试
}