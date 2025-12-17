package cn.silwings.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"cn.silwings"})
public class DynamicMockAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicMockAdminApplication.class, args);
    }

}
