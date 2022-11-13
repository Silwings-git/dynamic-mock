package top.silwings.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName DynamicMockConfig
 * @Description 配置类
 * @Author Silwings
 * @Date 2022/11/13 12:02
 * @Since
 **/
@Configuration
@EnableConfigurationProperties({TaskSchedulerProperties.class})
public class DynamicMockConfig implements AsyncConfigurer {

    @Bean
    public IdGenerator idGenerator() {
        return new JdkIdGenerator();
    }

    @Bean("asyncTaskPool")
    public Executor asyncTaskPool(final TaskSchedulerProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix("MockTask-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}