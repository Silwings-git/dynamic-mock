package top.silwings.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Slf4j
@EnableSwagger2WebMvc
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"top.silwings"})
public class DynamicMockAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicMockAdminApplication.class, args);
    }

}
