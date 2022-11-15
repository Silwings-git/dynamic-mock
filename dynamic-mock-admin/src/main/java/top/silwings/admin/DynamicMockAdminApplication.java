package top.silwings.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"top.silwings.core","top.silwings.admin"})
public class DynamicMockAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicMockAdminApplication.class, args);
    }

}
