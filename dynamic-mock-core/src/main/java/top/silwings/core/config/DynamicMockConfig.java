package top.silwings.core.config;

import io.netty.channel.ChannelOption;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;
import top.silwings.core.converter.NonString2TextHttpMessageConverter;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName DynamicMockConfig
 * @Description 配置类
 * @Author Silwings
 * @Date 2022/11/13 12:02
 * @Since
 **/
@Configuration
@EnableConfigurationProperties({TaskSchedulerProperties.class, MockTaskLogProperties.class, MockTaskHttpProperties.class})
public class DynamicMockConfig implements WebMvcConfigurer {

    @Bean
    public IdGenerator idGenerator() {
        return new JdkIdGenerator();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(final TaskSchedulerProperties properties) {
        final ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(properties.getCorePoolSize());
        executor.setThreadNamePrefix("MockTask-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    @Bean
    public WebClient webClient(final MockTaskHttpProperties mockTaskHttpProperties) {

        // 设置连接超时时间和读取超时时间
        final HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(mockTaskHttpProperties.getResponseTimeout()))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, mockTaskHttpProperties.getConnectTimeout());

        final ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .defaultHeader("Requester", "Dynamic-Mock-Service")
                .clientConnector(connector)
                .build();
    }

    @Override
    public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
        // 添加自定义的HttpMessageConverter
        converters.add(new NonString2TextHttpMessageConverter());
    }

}