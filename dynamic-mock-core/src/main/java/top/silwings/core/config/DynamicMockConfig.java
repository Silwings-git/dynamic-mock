package top.silwings.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName DynamicMockConfig
 * @Description 配置类
 * @Author Silwings
 * @Date 2022/11/13 12:02
 * @Since
 **/
@Configuration
@EnableConfigurationProperties({TaskSchedulerProperties.class})
public class DynamicMockConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new JdkIdGenerator();
    }

    @Bean("httpTaskScheduler")
    public TaskScheduler httpTaskScheduler(final TaskSchedulerProperties properties) {
        final ThreadPoolTaskScheduler httpTaskScheduler = new ThreadPoolTaskScheduler();
        httpTaskScheduler.setPoolSize(properties.getThreadPoolSize());
        return httpTaskScheduler;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}