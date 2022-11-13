package top.silwings.core.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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
public class DynamicMockConfig implements AsyncConfigurer, WebMvcConfigurer {

    @Value("${project.version}")
    private String projectVersion;

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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Dynamic-Mock Restful API")
                .description("Dynamic-Mock restful api")
                .termsOfServiceUrl("https://gitee.com/silwings")
                .contact(new Contact("Silwings", "https://gitee.com/silwings", "silwings@163.com"))
                .version(this.projectVersion)
                .build();
    }

    @Bean
    public Docket createRestApi(final ApiInfo apiInfo) {
        return new Docket(DocumentationType.SPRING_WEB)
                .apiInfo(apiInfo)
                .groupName("core")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

}